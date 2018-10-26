package helper.database.cassandra;

import com.google.common.base.MoreObjects;
import helper.database.Entity;
import helper.database.Paging;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * 基于 Cassandra 驱动的分页。
 *
 * @author qiang.zhang
 */
public final class CassandraPaging<T extends Entity> implements Paging<T> {
  static <T extends Entity> Paging<T> of(long total, int index, int count,
      @Nullable List<T> resources) {
    CassandraPaging<T> paging = new CassandraPaging<>();
    paging.total = total < 0 ? (resources != null ? resources.size() : 0) : total;
    paging.index = index < 0 ? 0 : index;
    paging.count = count < 0 ? 0 : index;
    paging.resources = resources == null ? Collections.emptyList() : resources;
    return paging;
  }

  private long total;
  private int index;
  private int count;
  private List<T> resources;

  @Override public long total() {
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
        .add("total", total)
        .add("index", index)
        .add("count", count)
        .add("resources", resources)
        .toString();
  }
}
