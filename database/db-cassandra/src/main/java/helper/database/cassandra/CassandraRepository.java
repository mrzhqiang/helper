package helper.database.cassandra;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.datastax.driver.mapping.Mapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import helper.database.Paging;
import helper.database.Repository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基于 Cassandra 的抽象仓库。
 * <p>
 * 简化 CQL 语句的相关操作。
 *
 * @author qiang.zhang
 */
public abstract class CassandraRepository<E extends CassandraEntity> implements Repository<E> {
  private static final Logger LOGGER = LoggerFactory.getLogger("cassandra");

  protected final Class<E> entityClass;

  /**
   * 由实体类型构造的 Cassandra 仓库。
   *
   * @param entityClass 实体类型。
   */
  protected CassandraRepository(Class<E> entityClass) {
    Preconditions.checkNotNull(entityClass);

    this.entityClass = entityClass;
  }

  /**
   * Cassandra 驱动。
   * <p>
   * 通常是由实现类提供的单例对象。
   *
   * @return Cassandra 驱动。
   */
  abstract protected Cassandra cassandra();

  /**
   * 根据实体映射器以及查询子句，获得资源总数量。
   *
   * @param mapper 实体映射器。
   * @param clauses 查询子句。
   * @return 0 表示没有任何资源，大于 0 表示查询到的资源总数量。
   */
  protected Long countAll(Mapper<E> mapper, List<Clause> clauses) {
    Session session = mapper.getManager().getSession();
    String keyspace = mapper.getTableMetadata().getKeyspace().getName();
    String table = mapper.getTableMetadata().getName();
    Select.Where countWhere = QueryBuilder.select().countAll().from(keyspace, table).where();
    for (Clause c : clauses) {
      countWhere.and(c);
    }
    LOGGER.debug("Execute count cql: {}", countWhere);
    ResultSet countSet = session.execute(countWhere);
    long total = 0L;
    if (countSet.getAvailableWithoutFetching() > 0) {
      total = countSet.one().getLong("count");
    }
    LOGGER.debug("keyspace {} table {} count all {}", keyspace, table, total);
    return total;
  }

  /**
   * 将 Map 对象转换为查询子句列表。
   *
   * @param map 包含查询子句键值的 Map 对象。
   * @return 查询子句列表。不为 Null，但有可能是 Empty。
   */
  protected List<Clause> ofMap(@Nullable Map<String, Object> map) {
    List<Clause> clauses = map == null ?
        Collections.emptyList() : Lists.newArrayListWithCapacity(map.size());
    if (map != null) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String key = entry.getKey();
        if (Strings.isNullOrEmpty(key)) {
          continue;
        }
        Object value = entry.getValue();
        if (value != null) {
          clauses.add(QueryBuilder.eq(key, value));
        }
      }
    }
    return clauses;
  }

  /**
   * 通过实体映射器、第一行序号、最大行数以及查询子句，获得资源列表。
   *
   * @param mapper 实体映射器。
   * @param firstRow 第一行序号。
   * @param maxRows 最大行数。
   * @param clauses 查询子句。
   * @return 资源列表。如果没有查询到，则返回 Null。
   */
  @Nullable
  protected List<E> resource(Mapper<E> mapper, int firstRow, int maxRows, List<Clause> clauses) {
    Session session = mapper.getManager().getSession();
    String keyspace = mapper.getTableMetadata().getKeyspace().getName();
    String table = mapper.getTableMetadata().getName();
    Select.Where where = QueryBuilder.select().from(keyspace, table).where();
    for (Clause c : clauses) {
      where.and(c);
    }
    LOGGER.debug("Execute list cql: {}", where);
    if (firstRow <= 1) {
      // 第一页的第一行，只需要限制最大行数
      ResultSet resultSet = session.execute(where.limit(maxRows));
      return mapper.map(resultSet).all();
    }
    // 获取当前页面第一行之前的分页状态
    PagingState pagingState =
        session.execute(where.setFetchSize(firstRow - 1))
            .getExecutionInfo()
            .getPagingState();
    // 根据分页状态获取当前页面的结果
    ResultSet resultSet =
        session.execute(where.setFetchSize(maxRows)
            .setPagingState(pagingState));
    // 如果有可用的结果
    if (resultSet.getAvailableWithoutFetching() > 0) {
      int fetchSize = resultSet.getAvailableWithoutFetching();
      List<E> resource = Lists.newArrayListWithCapacity(fetchSize);
      // for 循环添加到列表，防止 all 语句获取全部结果
      for (int i = 0; i < fetchSize; i++) {
        // 全部获取完毕，或当前页面无结果
        if (resultSet.isExhausted() || resultSet.getAvailableWithoutFetching() == 0) {
          break;
        }
        resource.add(mapper.map(resultSet).one());
      }
      return resource;
    }
    return null;
  }

  @Override public void save(E entity) {
    Preconditions.checkNotNull(entity);

    LOGGER.debug("Save entity: {}", entity);
    cassandra().mapper(entityClass).ifPresent(mapper -> mapper.save(entity));
  }

  @Override public void delete(Object... primaryKey) {
    Preconditions.checkNotNull(primaryKey);
    Preconditions.checkArgument(primaryKey.length > 0);

    LOGGER.debug("Delete primary key: {}", Arrays.toString(primaryKey));
    cassandra().mapper(entityClass).ifPresent(mapper -> mapper.delete(primaryKey));
  }

  @Override public Optional<E> get(Object... primaryKey) {
    Preconditions.checkNotNull(primaryKey);
    Preconditions.checkArgument(primaryKey.length > 0);

    LOGGER.debug("Get primary key: {}", Arrays.toString(primaryKey));
    return cassandra().mapper(entityClass).map(mapper -> mapper.get(primaryKey));
  }

  @Override
  public Paging<E> list(int index, int size, @Nullable Map<String, Object> clause) {
    LOGGER.debug("paging index: {} size: {} clause: {}", index, size, clause);
    int firstRow = Paging.computeFirstRow(index, size);
    int maxRows = Paging.computeMaxRows(size);
    return cassandra().mapper(entityClass)
        .map(mapper -> countAll(mapper, ofMap(clause)))
        .filter(total -> total > 0 && firstRow <= total)
        .map(total -> {
          int count = Paging.computePageCount(total, maxRows);
          List<E> resources = cassandra().mapper(entityClass)
              .map(mapper -> resource(mapper, firstRow, maxRows, ofMap(clause)))
              .orElse(Collections.emptyList());
          return Paging.of(total, index, count, resources);
        }).orElse(Paging.ofEmpty(index));
  }
}
