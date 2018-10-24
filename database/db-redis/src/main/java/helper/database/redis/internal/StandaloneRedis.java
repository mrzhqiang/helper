package helper.database.redis.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import helper.database.Databases;
import helper.database.redis.Redis;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BinaryJedis;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Protocol;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * 单机版 Redis 客户端。
 *
 * @author mrzhqiang
 */
@Singleton
public final class StandaloneRedis implements Redis {
  private static final Logger LOGGER = LoggerFactory.getLogger("redis");

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

    Config root = config.getConfig(ROOT_PATH);
    enabled = root.hasPath(ENABLED) && root.getBoolean(ENABLED);
    if (!enabled) {
      LOGGER.warn("Redis is not enabled.");
      return;
    }

    String host = root.hasPath(HOST) ? root.getString(HOST) : DEFAULT_HOST;
    int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
    int timeout = root.hasPath(TIMEOUT) ? root.getInt(TIMEOUT) : DEFAULT_TIMEOUT;
    boolean b = root.hasPathOrNull(PASSWORD) && !root.getIsNull(PASSWORD);
    String password = b ? root.getString(PASSWORD) : DEFAULT_PASSWORD;
    int database = root.hasPath(DATABASE) ? root.getInt(DATABASE) : DEFAULT_DATABASE;

    jedisPool = Databases.create(() ->
        new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database));
    LOGGER.info("Redis create successful.");
    find(BinaryJedis::ping)
        .ifPresent(s -> LOGGER.debug("Redis connect status: {}", "PONG".equals(s)));
    LOGGER.info("Redis is normal.");
  }

  @Override public void execute(Consumer<Jedis> consumer) {
    if (!enabled) {
      LOGGER.error("Redis is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Jedis resource = jedisPool.getResource()) {
      Databases.execute(resource, consumer);
    }
  }

  @Override public void pipelined(Consumer<Pipeline> consumer) {
    if (!enabled) {
      LOGGER.error("Redis is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Jedis resource = jedisPool.getResource()) {
      Pipeline pipelined = resource.pipelined();
      Databases.execute(pipelined, consumer);
      pipelined.sync();
    }
  }

  @Override public <T> Optional<T> find(Function<Jedis, T> function) {
    if (!enabled) {
      LOGGER.error("Redis is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    try (Jedis resource = jedisPool.getResource()) {
      return Optional.ofNullable(Databases.map(resource, function));
    }
  }

  @Override
  public <T> Optional<Response<T>> multi(Function<Transaction, T> function, String... watchs) {
    return Optional.empty();
  }
}
