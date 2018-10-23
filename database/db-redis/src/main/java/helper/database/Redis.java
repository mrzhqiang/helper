package helper.database;

import com.google.inject.ImplementedBy;
import helper.database.internal.StandaloneRedis;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;

/**
 * Redis 客户端。
 * <p>
 * 目前只有单机版。
 * <p>
 * 要启用 Redis，请增加模块的 reference.conf 文件或项目的 application.conf 文件，然后设定：
 * <pre>
 *   redis.enabled = true
 * </pre>
 * 即可启动。
 * <p>
 * 如果有不同配置，比如端口，可以设定：
 * <pre>
 *   redis.port = 6380
 * </pre>
 * 其中 6380 必须是你机器上的 Redis 数据库实例所开放的端口。
 *
 * @author mrzhqiang
 */
@ImplementedBy(StandaloneRedis.class)
public interface Redis {

  /**
   * 执行方法。
   * <p>
   * 此方法自动释放 {@link Jedis} 资源。
   * <p>
   * 注意：此方法适用复杂逻辑场景，前提于此，你可以做任何事情。
   * <p>
   * 当然，你也可以用 {@link Jedis}来开启事务功能，那么你就得自己做事务最后的执行操作。
   *
   * @param consumer 包裹 {@link Jedis} 的消费者对象。
   */
  void execute(Consumer<Jedis> consumer);

  /**
   * 流水线执行方法。
   * <p>
   * 此方法自动同步流水线并关闭 {@link Jedis} 资源，使用者应该专注于需要实现的逻辑。
   * <p>
   * 通过使用流水线执行多个命令（注意，必须是不需要结果的操作）可以降低与 Redis 之间通信的往返次数，
   * 它所提升的性能，在《Redis 实战》这本书中提到，大约是逐个执行命令的 4--5 倍。
   * <p>
   * 注意：此方法适用需要快速执行的批量提交场景，对于依赖执行结果的场景并不推荐。
   *
   * @param consumer 包裹 {@link Pipeline} 的消费者对象。
   */
  void pipelined(Consumer<Pipeline> consumer);

  /**
   * 查询某个对象。
   * <p>
   * 返回值是可选类型，这意味着有可能找不到任何对象，所以需要检查一下状态。
   * <p>
   * 此方法自动释放 {@link Jedis} 资源。
   * <p>
   * 注意：此方法适用复杂逻辑场景，前提于此，你可以做任何事情，但通常是立即获取结果的单独执行的命令。
   * <p>
   * 当然，你也可以用 {@link Jedis}来开启事务功能，那么你就得自己做事务最后的执行操作。
   *
   * @param function 包裹 {@link Jedis} 的函数对象。
   * @return 可选的结果对象。
   */
  <T> Optional<T> find(Function<Jedis, T> function);
}
