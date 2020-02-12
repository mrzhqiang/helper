package helper.data.redis;

import com.google.inject.ImplementedBy;
import helper.data.redis.internal.StandaloneRedis;
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
   *
   * @param consumer 包裹 {@link Jedis} 的消费者对象。
   */
  void execute(Consumer<Jedis> consumer);

  /**
   * 流水线执行方法。
   * <p>
   * 此方法自动同步流水线，并释放 {@link Jedis} 资源，使用者应该专注于逻辑的实现。
   * <p>
   * 通过使用流水线执行多个命令（结果不敏感），可以降低与 Redis 之间通信的往返次数，
   * 它所提升的性能，在《Redis 实战》这本书中有提到，大概是逐个执行命令的 4--5 倍。
   * <p>
   * 提示：一般来说，流水线执行的是一系列命令，它们互相之间可以没有关联，因此用不到事务。
   *
   * @param consumer 包裹 {@link Pipeline} 的消费者对象。
   */
  void pipelined(Consumer<Pipeline> consumer);

  /**
   * 查询方法。
   * <p>
   * 此方法自动释放 {@link Jedis} 资源。
   * <p>
   * 返回类型是可选的，这意味着有可能不存在返回值，所以需要检查一下状态。
   *
   * @param function 包裹 {@link Jedis} 的函数对象。
   * @param <R> 返回类型。
   * @return 可选的结果对象。
   */
  <R> Optional<R> find(Function<Jedis, R> function);

  /**
   * 事务查询某个对象。
   * <p>
   * 此方法自动提交事务，并释放 {@link Jedis} 资源，使用者应该专注于逻辑的实现。
   * <p>
   * 通过使用事务执行多个命令，可以在提升性能的同时，保证操作对象的前后一致性，
   * 特别是加入监视键功能后，一旦监视的键被修改，则事务不会提交成功。
   * <p>
   * 返回类型是可选的，这意味着有可能不存在返回值，所以需要检查一下状态。
   * <p>
   * 提示：一般来说，事务提交的是多个互相之间强关联的命令，比如商品交易的进出项。
   *
   * @param function 包裹 {@link Transaction} 的函数对象。
   * @param watchKeys 监视键的数组。
   * @param <R> 返回类型。
   * @return 可选的响应结果对象。
   */
  <R> Optional<Response<R>> multi(Function<Transaction, Response<R>> function, String... watchKeys);
}
