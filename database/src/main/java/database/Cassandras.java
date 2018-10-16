package database;

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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;

import static database.Util.*;

/**
 * Cassandra 驱动工具包。
 *
 * @author qiang.zhang
 */
final class Cassandras {
  private Cassandras() {
    throw new AssertionError("No instance.");
  }

  /**
   * 通过实体映射器和查询语句，获取实体的全部数据。
   * <p>
   * 注意：这个方法有可能导致查询超时，需要很高的性能，请谨慎使用。
   *
   * @param mapper 实体映射器。
   * @param clause 查询语句。
   * @param <T> 实体对象。
   * @return 实体列表。
   */
  static <T> List<T> list(Mapper<T> mapper, @Nullable Map<String, Object> clause) {
    Preconditions.checkNotNull(mapper);

    ResultSet resultSet = mapper.getManager()
        .getSession()
        .execute(select(mapper, ofMap(clause)));
    return mapper.map(resultSet).all();
  }

  /**
   * 通过页面序号、页面大小、实体映射器、查询语句，获得关于实体的分页状态。
   * <p>
   * 注意：这个方法应该用 {@link Databases#create(Supplier)}  DatabaseHelper.create} 进行保护性调用。
   *
   * @param index 页面序号。
   * @param size 页面大小，最小是 10。对数据行进行分页是非常消耗性能的，不允许低于 10 的值。
   * @param mapper 实体映射器。
   * @param clause 正常查询语句。
   * @return Cassandra 分页对象，可以进行 Json 序列化。
   * @see CassandraPaging
   */
  static <T> Paging<T> paging(int index, int size, Mapper<T> mapper,
      @Nullable Map<String, Object> clause) {
    Preconditions.checkNotNull(mapper);

    int total = -1;
    int firstRow = computeFirstRow(index, size);
    int maxRows = computeMaxRows(size);

    Session session = mapper.getManager().getSession();
    Select.Where countWhere = countAll(mapper, ofMap(clause));
    ResultSet countSet = session.execute(countWhere);
    if (countSet.getAvailableWithoutFetching() > 0) {
      total = countSet.one().getInt("count");
    }
    int count = computePageCount(total, maxRows);

    Select.Where where = select(mapper, ofMap(clause));
    if (total > 0) {
      if (firstRow == 1) {
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
        List<T> resource = Lists.newArrayListWithCapacity(fetchSize);
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
    return CassandraPaging.of(total, index, count, Collections.emptyList());
  }

  /**
   * 根据实体映射器和查询子句，生成一个统计所有数据量的查询语句。
   *
   * @param mapper 实体映射器。
   * @param clauses 查询子句。
   * @param <T> 实体类型。
   * @return 查询语句。
   */
  private static <T> Select.Where countAll(Mapper<T> mapper,
      @Nullable List<Clause> clauses) {
    String keyspace = mapper.getTableMetadata().getKeyspace().getName();
    String table = mapper.getTableMetadata().getName();
    Select.Where where = QueryBuilder.select().countAll().from(keyspace, table).where();
    if (clauses != null) {
      for (Clause clause : clauses) {
        where.and(clause);
      }
    }
    return where;
  }

  /**
   * 根据实体映射器和查询子句，生成一个获取所有数据的查询语句。
   *
   * @param <T> 实体类型。
   * @param mapper 实体映射器。
   * @param clauses 查询子句。
   * @return 查询语句。
   */
  private static <T> Select.Where select(Mapper<T> mapper, @Nullable List<Clause> clauses) {
    String keyspace = mapper.getTableMetadata().getKeyspace().getName();
    String table = mapper.getTableMetadata().getName();
    Select.Where where = QueryBuilder.select().from(keyspace, table).where();
    if (clauses != null) {
      for (Clause clause : clauses) {
        where.and(clause);
      }
    }
    return where;
  }

  /**
   * 通过 map 对象生成查询子句列表。
   *
   * @param map 主键列名和主键值的键值对。
   * @return 查询子句。
   */
  private static List<Clause> ofMap(@Nullable Map<String, Object> map) {
    List<Clause> clauses =
        map == null ? Collections.emptyList() : Lists.newArrayListWithCapacity(map.size());
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
}
