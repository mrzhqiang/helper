package helper.database;

import com.google.inject.Guice;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.BinaryJedis;

import static org.junit.Assert.*;

/**
 * Redis 接口的单元测试。
 * <p>
 * 首先必须启动 Redis，其次检测端口是否默认为 6379，然后运行此单元测试类。
 * <p>
 * 关于修改配置：
 * <pre>
 *   // 启动
 *   redis.enabled = true
 *   // 端口
 *   redis.port = 6380
 *   // 其他...
 * </pre>
 *
 * @author mrzhqiang
 */
public final class RedisTest {
  private Redis redis;

  @Before
  public void setUp() {
    redis = Guice.createInjector().getInstance(Redis.class);
  }

  @After
  public void tearDown() {
    redis = null;
  }

  @Test
  public void execute() {
    redis.execute(jedis -> assertEquals("PONG", jedis.ping()));
  }

  @Test
  public void find() {
    redis.find(BinaryJedis::ping).ifPresent(s -> assertEquals("PONG", s));
  }
}