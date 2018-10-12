package helper;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

/**
 * 单机版 Redis 客户端。
 *
 * @author mrzhqiang
 */
@Singleton final class StandaloneRedis implements Redis {
  private static final Logger LOGGER = LogManager.getLogger("database");

  private static final String ROOT_PATH = "redis";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String TIMEOUT = "timeout";
  private static final String PASSWORD = "password";
  private static final String DATABASE = "database";
  private static final String ENABLED = "enabled";

  private static final String DEFAULT_HOST = Protocol.DEFAULT_HOST;
  private static final int DEFAULT_PORT = Protocol.DEFAULT_PORT;
  private static final int DEFAULT_TIMEOUT = Protocol.DEFAULT_TIMEOUT;
  private static final String DEFAULT_PASSWORD = null;
  private static final int DEFAULT_DATABASE = Protocol.DEFAULT_DATABASE;

  private boolean enabled = false;
  private JedisPool jedisPool;

  public StandaloneRedis() {
    Config config = ConfigFactory.load();
    if (!config.hasPath(ROOT_PATH)) {
      return;
    }

    Config rootConfig = config.getConfig(ROOT_PATH);
    enabled = rootConfig.hasPath(ENABLED) && rootConfig.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.info("Redis is not enabled.");
      return;
    }

    String host = rootConfig.hasPath(HOST) ? rootConfig.getString(HOST) : DEFAULT_HOST;
    int port = rootConfig.hasPath(PORT) ? rootConfig.getInt(PORT) : DEFAULT_PORT;
    int timeout = rootConfig.hasPath(TIMEOUT) ? rootConfig.getInt(TIMEOUT) : DEFAULT_TIMEOUT;
    String password = rootConfig.hasPathOrNull(PASSWORD) && !rootConfig.getIsNull(PASSWORD)
        ? rootConfig.getString(PASSWORD) : DEFAULT_PASSWORD;
    int database = rootConfig.hasPath(DATABASE) ? rootConfig.getInt(DATABASE) : DEFAULT_DATABASE;

    jedisPool = DatabaseHelper.create(() ->
        new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database));
    LOGGER.info("Redis create successful.");
    find(BinaryJedis::ping)
        .ifPresent(s -> LOGGER.info("Redis connect status: {}", "PONG".equals(s)));
    LOGGER.info("Redis is normal.");
  }

  @Override public void execute(Consumer<Jedis> consumer) {
    if (!enabled) {
      LOGGER.warn("Redis is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Jedis resource = jedisPool.getResource()) {
      DatabaseHelper.execute(resource, consumer);
    }
  }

  @Override public <T> Optional<T> find(Function<Jedis, T> function) {
    if (!enabled) {
      LOGGER.warn("Redis is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    try (Jedis resource = jedisPool.getResource()) {
      return DatabaseHelper.create(() -> Optional.ofNullable(function.apply(resource)));
    }
  }
}
