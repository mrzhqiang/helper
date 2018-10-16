package database;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import io.ebean.PagedList;
import java.util.List;

/**
 * 基于 EBean 的实体分页。
 *
 * @author qiang.zhang
 */
public final class EBeanPaging<E extends EBeanModel> implements Paging<E> {
  static <E extends EBeanModel> Paging<E> of(PagedList<E> list) {
    Preconditions.checkNotNull(list);

    list.loadCount();
    EBeanPaging<E> paging = new EBeanPaging<>();
    paging.total = list.getTotalCount();
    paging.index = list.getPageIndex();
    paging.size = list.getTotalPageCount();
    paging.resources = list.getList();
    return paging;
  }

  private int total;
  private int index;
  private int size;
  private List<E> resources;

  @Override public int total() {
    return total;
  }

  @Override public int index() {
    return index;
  }

  @Override public int count() {
    return size;
  }

  @Override public List<E> resources() {
    return resources;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("资源总计", total())
        .add("页面序号", index())
        .add("页面总计", count())
        .add("资源列表", resources())
        .toString();
  }
}
