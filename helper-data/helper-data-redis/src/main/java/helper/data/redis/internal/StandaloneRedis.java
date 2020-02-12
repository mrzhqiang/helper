package helper.data.redis.internal;

import com.google.common.base.Preconditions;
import com.google.inject.Singleton;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import helper.data.Util;
import helper.data.redis.Redis;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

import static org.apache.commons.pool2.impl.GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;
import static redis.clients.jedis.Protocol.DEFAULT_DATABASE;
import static redis.clients.jedis.Protocol.DEFAULT_HOST;
import static redis.clients.jedis.Protocol.DEFAULT_PORT;
import static redis.clients.jedis.Protocol.DEFAULT_TIMEOUT;

/**
 * 单机版 Redis 客户端。
 *
 * @author mrzhqiang
 */
@Slf4j
@Singleton
public final class StandaloneRedis implements Redis {
  private static final String ROOT_PATH = "redis";

  private static final String ENABLED = "enabled";
  private static final String HOST = "host";
  private static final String PORT = "port";
  private static final String TIMEOUT = "timeout";
  private static final String PASSWORD = "password";
  private static final String DATABASE = "database";
  private static final String CONNECT_COUNT = "connect-count";

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
      log.error("you want use Redis but it not be enabled.");
      return;
    }

    String host = root.hasPath(HOST) ? root.getString(HOST) : DEFAULT_HOST;
    int port = root.hasPath(PORT) ? root.getInt(PORT) : DEFAULT_PORT;
    int timeout = root.hasPath(TIMEOUT) ? root.getInt(TIMEOUT) : DEFAULT_TIMEOUT;
    String password = root.hasPathOrNull(PASSWORD) && !root.getIsNull(PASSWORD)
        ? root.getString(PASSWORD) : null;
    int database = root.hasPath(DATABASE) ? root.getInt(DATABASE) : DEFAULT_DATABASE;
    int connectCount = root.hasPath(CONNECT_COUNT)
        ? root.getInt(CONNECT_COUNT) : DEFAULT_MAX_TOTAL;

    JedisPoolConfig poolConfig = new JedisPoolConfig();
    poolConfig.setMaxTotal(connectCount);

    jedisPool = Util.create(() ->
        new JedisPool(poolConfig, host, port, timeout, password, database));
    log.info("Jedis drive create successful.");
    Boolean status = find(jedis -> "PONG".equalsIgnoreCase(jedis.ping())).orElse(false);
    log.debug("Redis connect status: {}", status);
  }

  @Override public void execute(Consumer<Jedis> consumer) {
    if (!enabled || jedisPool == null) {
      log.error("Redis is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Jedis resource = jedisPool.getResource()) {
      Util.execute(resource, consumer);
    }
  }

  @Override public void pipelined(Consumer<Pipeline> consumer) {
    if (!enabled || jedisPool == null) {
      log.error("Redis is disabled.");
      return;
    }

    Preconditions.checkNotNull(consumer);
    try (Jedis resource = jedisPool.getResource()) {
      Pipeline pipelined = resource.pipelined();
      Util.execute(pipelined, consumer);
      pipelined.sync();
    }
  }

  @Override public <T> Optional<T> find(Function<Jedis, T> function) {
    if (!enabled || jedisPool == null) {
      log.error("Redis is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    try (Jedis resource = jedisPool.getResource()) {
      return Optional.ofNullable(Util.transform(resource, function));
    }
  }

  @Override
  public <R> Optional<Response<R>> multi(Function<Transaction, Response<R>> function,
      String... watchKeys) {
    if (!enabled || jedisPool == null) {
      log.error("Redis is disabled.");
      return Optional.empty();
    }

    Preconditions.checkNotNull(function);
    boolean isWatch = watchKeys != null && watchKeys.length > 0;
    try (Jedis resource = jedisPool.getResource()) {
      if (isWatch) {
        // 提交事务时，自动取消观察
        resource.watch(watchKeys);
      }
      Transaction multi = resource.multi();
      Response<R> response = function.apply(multi);
      // 必须在返回之前执行事务
      multi.exec();
      return Optional.ofNullable(response);
    }
  }
}
