package database;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import io.ebean.PagedList;
import java.util.Map;
import javax.annotation.Nullable;

import static database.Util.*;

/**
 * EBean 工具包。
 *
 * @author qiang.zhang
 */
final class EBeans {
  private EBeans() {
    throw new AssertionError("No instance.");
  }

  /**
   * 通过 Finder 和 map 对象，生成实体表达式列表。
   *
   * @param finder 查询器。
   * @param map map 对象，即 查询条件子句。
   * @param <I> 主键类型。
   * @param <T> 实体类型。
   * @return 实体表达式列表。
   */
  static <I, T> ExpressionList<T> ofMap(Finder<I, T> finder, @Nullable Map<String, Object> map) {
    Preconditions.checkNotNull(finder);

    ExpressionList<T> where = finder.query().where();
    if (map != null) {
      for (Map.Entry<String, Object> entry : map.entrySet()) {
        String key = entry.getKey();
        if (Strings.isNullOrEmpty(key)) {
          continue;
        }
        Object value = entry.getValue();
        if (value != null) {
          where.eq(key, value);
        }
      }
    }
    return where;
  }

  /**
   * 通过首行行号、最大行数、实体表达式列表，获得关于实体的分页状态。
   *
   * @param index 页面序号。
   * @param size 页面大小，最小为 10。
   * @param where 实体表达式列表。
   * @param <T> 基于 EbeanModel 的实体类型。
   * @return 分页状态。
   * @see EBeanPaging
   */
  static <T extends EBeanModel> Paging<T> paging(int index, int size, ExpressionList<T> where) {
    Preconditions.checkNotNull(where);

    int firstRow = computeFirstRow(index, size);
    int maxRows = computeMaxRows(size);
    PagedList<T> pagedList = where
        .setFirstRow(firstRow)
        .setMaxRows(maxRows)
        .findPagedList();
    return EBeanPaging.of(pagedList);
  }
}
