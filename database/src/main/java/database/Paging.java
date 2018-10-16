package database;

import java.util.List;

/**
 * 分页。
 *
 * @author mrzhqiang
 */
public interface Paging<E> {
  /**
   * 资源的总计数量。
   *
   * @return 资源总数。
   */
  int total();

  /**
   * 页面的当前序号。
   *
   * @return 页面序号。
   */
  int index();

  /**
   * 页面的总计数量。
   *
   * @return 页面总数。
   */
  int count();

  /**
   * 当前页面的资源列表。
   *
   * @return 资源列表。
   */
  List<E> resources();
}
