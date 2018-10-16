package database;

import com.datastax.driver.core.Session;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.CreateIndex;
import com.datastax.driver.core.schemabuilder.CreateKeyspace;
import com.datastax.driver.core.schemabuilder.CreateType;
import com.datastax.driver.mapping.Mapper;
import com.google.inject.ImplementedBy;
import java.util.function.Consumer;
import javax.annotation.Nullable;

/**
 * Cassandra 数据库驱动。
 *
 * @author qiang.zhang
 */
@ImplementedBy(StandaloneCassandra.class)
public interface Cassandra {

  /**
   * 执行 CQL 语句，通常是 {@link CreateKeyspace 创建秘钥空间}、{@link CreateType 创建自定义类型}
   * 、{@link Create 创建表格} 以及 {@link CreateIndex 创建索引} 这些常用语句。
   * <p>
   * 提示：此方法结束后，将自动释放 {@link Session} 资源。
   *
   * @param consumer 包裹 {@link Session} 的消费者对象。
   */
  void execute(Consumer<Session> consumer);

  /**
   * 获得 {@link Mapper 实体映射器}，由此映射器处理实体的 CURD 操作。
   * <p>
   * 提示：实体所包含的 {@link Session} 不能被关闭，否则后续将无法访问数据库。
   *
   * @param clazz 实体的 Class 对象。
   * @return 实体映射器。
   */
  @Nullable <E> Mapper<E> mapper(Class<E> clazz);
}
