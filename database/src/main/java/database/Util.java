package database;

/**
 * 内部工具。
 *
 * @author qiang.zhang
 */
final class Util {
  private Util() {
    throw new AssertionError("No instance.");
  }

  /**
   * 计算指定页面的第一行序号。
   *
   * @param index 指定页面索引。
   * @param size 页面大小。
   * @return 第一行序号。
   */
  static int computeFirstRow(int index, int size) {
    return Math.max(((index - 1) * computeMaxRows(size) + 1), 1);
  }

  /**
   * 通过页面大小，计算当前页面的最大行数。
   * <p>
   * 注意：最大行数的最小值为 10。
   *
   * @param size 页面大小。
   * @return 修正的页面大小。
   */
  static int computeMaxRows(int size) {
    return Math.max(size, 10);
  }

  /**
   * 计算页面数量。
   *
   * @param total 总行数。
   * @param maxRows 页面最大行数。
   * @return 页面数量。
   */
  static int computePageCount(int total, int maxRows) {
    if (total <= 0) {
      return 0;
    } else {
      return ((total - 1) / maxRows) + 1;
    }
  }
}
