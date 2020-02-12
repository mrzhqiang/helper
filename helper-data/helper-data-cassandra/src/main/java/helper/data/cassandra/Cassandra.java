package helper.data.cassandra;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.google.inject.ImplementedBy;
import helper.data.cassandra.internal.StandaloneCassandra;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Cassandra 驱动。
 * <p>
 * 目前只有单机版，事实上 Cassandra 集群只需要单机版。
 * <p>
 * 要启用 Cassandra，请在模块下增加 reference.conf 文件，或在项目下增加 application.conf 文件，然后
 * 设定：
 * <pre>
 *   cassandra.enabled = true
 * </pre>
 * 即可启动。
 * <p>
 * 若想设定不同的配置，比如主机地址、端口，可以这样做：
 * <pre>
 *   cassandra.host = "192.168.1.100"
 *   cassandra.port = 9043
 * </pre>
 * 其中，192.168.1.100 和 9043 必须是 Cassandra 数据库实例的主机地址和端口。
 *
 * @author mrzhqiang
 */
@ImplementedBy(StandaloneCassandra.class)
public interface Cassandra {

  /**
   * 执行 CQL 语句，通常是 Keyspace、Type 以及 Table 的创建语句。
   * <p>
   * 此方法自动释放 {@link Session} 资源。
   *
   * @param consumer 包裹 Session 的消费者对象。
   */
  void execute(Consumer<Session> consumer);

  /**
   * 获得实体映射器，进行简单的 CURD 操作。
   *
   * @param clazz 实体类型。
   * @return 实体映射器。
   */
  <T> Optional<Mapper<T>> mapper(Class<T> clazz);
}
