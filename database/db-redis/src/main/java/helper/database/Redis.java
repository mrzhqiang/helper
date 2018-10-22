package helper.database;

import com.google.inject.ImplementedBy;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.Jedis;

/**
 * Redis 客户端。
 * <p>
 * 目前只有单机版。
 *
 * @author mrzhqiang
 */
@ImplementedBy(StandaloneRedis.class)
public interface Redis {
  /**
   * 执行方法。
   * <p>
   * 通常是 set 等操作。
   * <p>
   * 此方法自动释放 {@link Jedis} 资源。
   *
   * @param consumer 包裹 {@link Jedis} 的消费者对象。
   */
  void execute(Consumer<Jedis> consumer);

  /**
   * 查询某个对象。
   * <p>
   * 返回值是可选类型，这意味着有可能找不到任何对象，所以需要检查一下状态。
   * <p>
   * 此方法自动释放 {@link Jedis} 资源。
   *
   * @param function 包裹 {@link Jedis} 的功能对象。
   * @return 可选的对象。
   */
  <T> Optional<T> find(Function<Jedis, T> function);
}
