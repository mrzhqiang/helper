package helper.database;

import java.util.List;

/**
 * 分页。
 * <p>
 * 大多数数据库本身带有分页功能，只不过有些使用简单，有些显得复杂，所以我们使用分页接口来规范化。
 *
 * @param <E> 实体类型。
 * @author mrzhqiang
 */
public interface Paging<E extends Entity> {

  /**
   * 获取资源总数。
   *
   * @return 资源总数，64 位整型。
   */
  long total();

  /**
   * 获取页面序号。
   *
   * @return 页面序号，32 位整型。
   */
  int index();

  /**
   * 获取页面总数。
   *
   * @return 页面总数，32 位整型。
   */
  int count();

  /**
   * 获取资源列表。
   *
   * @return 资源列表。
   */
  List<E> resources();

  /**
   * 计算页面的最大行数。
   *
   * @param size 页面大小。
   * @return 最大行数。最小值为 10，最大值为 2000。这是为了防止程序被恶意破坏。
   */
  static int computeMaxRows(int size) {
    return Math.min(2000, Math.max(10, size));
  }

  /**
   * 计算页面的首行序号。
   *
   * @param index 页面索引。
   * @param maxRows 页面最大行数。
   * @return 首行序号。最小值为 1。
   */
  static int computeFirstRow(int index, int maxRows) {
    return Math.max(1, ((index - 1) * maxRows + 1));
  }

  /**
   * 计算页面的总数。
   *
   * @param total 总行数。
   * @param maxRows 页面最大行数。
   * @return 页面数量。
   */
  static int computePageCount(long total, int maxRows) {
    if (total <= 0) {
      return 0;
    } else {
      return (int) (((total - 1) / maxRows) + 1);
    }
  }
}
