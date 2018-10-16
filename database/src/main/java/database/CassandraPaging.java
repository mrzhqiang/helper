package database;

import com.google.common.base.MoreObjects;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * 基于 Cassandra 驱动的分页。
 * <p>
 * 这个类可以用于 Json 序列化。
 *
 * @author qiang.zhang
 */
public final class CassandraPaging<T> implements Paging<T> {
  static <T> Paging<T> of(int total, int index, int count, @Nullable List<T> resources) {
    CassandraPaging<T> paging = new CassandraPaging<>();
    paging.total = total < 0 ? (resources != null ? resources.size() : 0) : total;
    paging.index = index < 0 ? 0 : index;
    paging.count = count < 0 ? 0 : index;
    paging.resources = resources == null ? Collections.emptyList() : resources;
    return paging;
  }

  private int total;
  private int index;
  private int count;
  private List<T> resources;

  @Override public int total() {
    return total;
  }

  @Override public int index() {
    return index;
  }

  @Override public int count() {
    return count;
  }

  @Override public List<T> resources() {
    return resources;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("资源总计", total)
        .add("页面序号", index)
        .add("页面总计", count)
        .add("资源列表", resources)
        .toString();
  }
}
