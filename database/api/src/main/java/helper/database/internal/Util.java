package helper.database.internal;

/**
 * 内部工具。
 *
 * @author qiang.zhang
 */
public final class Util {
  private Util() {
    throw new AssertionError("No instance.");
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
   * 计算指定页面的首行序号。
   *
   * @param index 页面索引。
   * @param maxRows 页面最大行数。
   * @return 首行序号。最小值为 1。
   */
  public static int computeFirstRow(int index, int maxRows) {
    return Math.max(1, ((index - 1) * maxRows + 1));
  }

  /**
   * 计算页面数量。
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
