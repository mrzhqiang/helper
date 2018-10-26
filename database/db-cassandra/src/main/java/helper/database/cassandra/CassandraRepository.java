package helper.database.cassandra;

import com.datastax.driver.core.PagingState;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.querybuilder.Select;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import helper.database.Entity;
import helper.database.Paging;
import helper.database.Repository;
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
public abstract class CassandraRepository<E extends Entity> implements Repository<E> {
  private static final Logger LOGGER = LoggerFactory.getLogger("cassandra");

  private final Class<E> entityClass;

  /**
   * 由实体类型构造的 Cassandra 仓库。
   *
   * @param entityClass 实体类型。
   */
  protected CassandraRepository(Class<E> entityClass) {
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

  private List<Clause> ofMap(@Nullable Map<String, Object> map) {
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

  @Override public void save(E entity) {
    cassandra().mapper(entityClass).ifPresent(mapper -> mapper.save(entity));
  }

  @Override public void delete(Object... primaryKey) {
    cassandra().mapper(entityClass).ifPresent(mapper -> mapper.delete(primaryKey));
  }

  @Override public Optional<E> get(Object... primaryKey) {
    return cassandra().mapper(entityClass).map(mapper -> mapper.get(primaryKey));
  }

  @Override
  public Paging<E> page(int index, int size, @Nullable Map<String, Object> clause) {
    int firstRow = Paging.computeFirstRow(index, size);
    int maxRows = Paging.computeMaxRows(size);
    return cassandra().mapper(entityClass)
        .map(mapper -> {
          long total = -1L;
          Session session = mapper.getManager().getSession();
          String keyspace = mapper.getTableMetadata().getKeyspace().getName();
          String table = mapper.getTableMetadata().getName();

          Select.Where countWhere = QueryBuilder.select().countAll().from(keyspace, table).where();
          List<Clause> clauses = ofMap(clause);
          for (Clause c : clauses) {
            countWhere.and(c);
          }
          ResultSet countSet = session.execute(countWhere);
          if (countSet.getAvailableWithoutFetching() > 0) {
            total = countSet.one().getLong("count");
          }
          if (total > 0) {
            int count = Paging.computePageCount(total, maxRows);
            Select.Where where = QueryBuilder.select().from(keyspace, table).where();
            for (Clause c : clauses) {
              where.and(c);
            }
            if (firstRow <= 1) {
              // 第一页的第一行，只需要限制最大行数
              ResultSet resultSet = session.execute(where.limit(maxRows));
              return CassandraPaging.of(total, index, count, mapper.map(resultSet).all());
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
              return CassandraPaging.of(total, index, count, resource);
            }
          }
          return null;
        })
        .orElse(CassandraPaging.of(0L, index, 0, null));
  }

  @Override public List<E> list(@Nullable Map<String, Object> clause) {
    return cassandra().mapper(entityClass)
        .map(mapper -> {
          String keyspace = mapper.getTableMetadata().getKeyspace().getName();
          String table = mapper.getTableMetadata().getName();
          Select.Where where = QueryBuilder.select().from(keyspace, table).where();
          for (Clause c : ofMap(clause)) {
            where.and(c);
          }
          ResultSet resultSet = mapper.getManager()
              .getSession()
              .execute(where);
          return mapper.map(resultSet).all();
        })
        .orElse(Collections.emptyList());
  }
}
