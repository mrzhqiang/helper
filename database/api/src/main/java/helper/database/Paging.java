package helper.database;

import java.util.List;

/**
 * 分页。
 * <p>
 * 一旦数据积累到足够的量，则每次查询所有结果将占据大量服务器资源，为此增加分页功能。
 *
 * @param <E> 资源映射的实体类型。
 * @author mrzhqiang
 */
public interface Paging<E> {
  /**
   * 资源的总数量。
   *
   * @return 资源总数。
   */
  long total();

  /**
   * 页面的序列号。
   *
   * @return 页面序号。
   */
  int index();

  /**
   * 页面的总数量。
   *
   * @return 页面数量。
   */
  int count();

  /**
   * 页面资源列表。
   *
   * @return 资源列表。
   */
  List<E> resources();
}
