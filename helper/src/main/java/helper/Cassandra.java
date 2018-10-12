package helper;

import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.google.inject.ImplementedBy;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * Cassandra 驱动。
 *
 * @author qiang.zhang
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
  @Nullable <T> Mapper<T> mapper(Class<T> clazz);
}
