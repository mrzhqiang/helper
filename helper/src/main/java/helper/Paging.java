package helper;

import java.util.List;

/**
 * 分页。
 * <p>
 * 建议：它的实现类应该具备 Json 序列化的能力。
 *
 * @author mrzhqiang
 */
public interface Paging<E> {
  /**
   * 表示资源的总数量。
   *
   * @return 资源总数。
   */
  int total();

  /**
   * 表示当前页面序号。
   *
   * @return 页面序号。
   */
  int index();

  /**
   * 表示页面的总数量。
   *
   * @return 页面数量。
   */
  int count();

  /**
   * 表示当前页面资源。
   *
   * @return 资源列表。
   */
  List<E> resources();
}
