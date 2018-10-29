package helper.database;

import com.google.common.base.MoreObjects;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;

/**
 * 分页。
 * <p>
 * 我们使用分页类来规范列表查询的返回形式。
 *
 * @param <R> 资源类型。
 * @author mrzhqiang
 */
public final class Paging<R> {
  /**
   * 根据指定参数生成分页。
   *
   * @param total 总数量。
   * @param index 页面序号。
   * @param count 页面数量。
   * @param resources 资源列表。
   * @param <R> 资源类型
   * @return 分页。
   */
  public static <R> Paging<R> of(long total, int index, int count, @Nullable List<R> resources) {
    Paging<R> paging = new Paging<>();
    paging.total = total < 0 ? (resources != null ? resources.size() : 0) : total;
    paging.index = index < 1 ? 1 : index;
    paging.count = count < 0 ? 0 : count;
    paging.resources = resources == null ? Collections.emptyList() : resources;
    return paging;
  }

  /**
   * 空的分页。
   * <p>
   * 查询不到就返回空的分页。
   *
   * @param index 页面序号。
   * @param <R> 资源类型。
   * @return 分页。
   */
  public static <R> Paging<R> ofEmpty(int index) {
    return of(0, index, 0, null);
  }

  /**
   * 总数量。
   * <p>
   * 数据库中可查到的统计数量。
   */
  public long total;
  /**
   * 页面序号。
   * <p>
   * 从 1 开始到 count 结束。
   */
  public int index;
  /**
   * 页面数量。
   * <p>
   * 总共划分多少个页面。
   */
  public int count;
  /**
   * 资源列表。
   */
  public List<R> resources;

  @Override public int hashCode() {
    return Objects.hash(total, index, count, resources);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Paging)) {
      return false;
    }

    Paging other = (Paging) obj;
    return Objects.equals(total, other.total)
        && Objects.equals(index, other.index)
        && Objects.equals(count, other.count)
        && Objects.equals(resources, other.resources);
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("total", total)
        .add("index", index)
        .add("count", count)
        .add("resources", resources)
        .toString();
  }

  /**
   * 计算页面的最大行数。
   *
   * @param size 页面大小。
   * @return 最大行数。最小值为 10，最大值为 2000。这是为了防止程序被恶意破坏。
   */
  public static int computeMaxRows(int size) {
    return Math.min(2000, Math.max(10, size));
  }

  /**
   * 计算页面的首行序号。
   *
   * @param index 页面索引。
   * @param maxRows 页面最大行数。
   * @return 首行序号。最小值为 1。
   */
  public static int computeFirstRow(int index, int maxRows) {
    return Math.max(1, ((index - 1) * maxRows + 1));
  }

  /**
   * 计算页面的总数。
   *
   * @param total 总行数。
   * @param maxRows 页面最大行数。
   * @return 页面数量。
   */
  public static int computePageCount(long total, int maxRows) {
    if (total <= 0) {
      return 0;
    } else {
      return (int) (((total - 1) / maxRows) + 1);
    }
  }
}
