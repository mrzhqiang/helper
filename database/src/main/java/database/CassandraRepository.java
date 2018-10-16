package database;

import com.datastax.driver.mapping.Mapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * 基于 Cassandra 的抽象仓库。
 * <p>
 * 这里用 Cassandra 实现 CRUD 等基本操作，包括分页列表以及获取全部数据的方法。
 *
 * @author qiang.zhang
 */
public abstract class CassandraRepository<T> implements Repository<T> {
  /**
   * Cassandra 实体映射器。
   *
   * @return 映射器对象。
   */
  abstract protected Mapper<T> mapper();

  @Override public void save(T entity) {
    Databases.execute(entity, t -> mapper().save(t));
  }

  @Override public void delete(Object... primaryKey) {
    Databases.execute(primaryKey, objects -> mapper().delete(objects));
  }

  @Override public Optional<T> get(Object... primaryKey) {
    return Databases.find(primaryKey, objects -> mapper().get(objects));
  }

  @Override
  public Paging<T> list(int index, int size, @Nullable Map<String, Object> clause) {
    return Databases.create(() -> Cassandras.paging(index, size, mapper(), clause));
  }

  @Override public List<T> list(@Nullable Map<String, Object> clause) {
    return Databases.create(() -> Cassandras.list(mapper(), clause));
  }
}
