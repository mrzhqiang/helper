package helper.database;

import com.google.common.base.MoreObjects;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * 基于 Redis 客户端的分页。
 * <p>
 * 分页类应该始终可以进行 Json 序列化。
 *
 * @author mrzhqiang
 */
public final class RedisPaging<T> implements Paging<T> {
  static <T> Paging<T> of(long total, int index, int count, @Nullable List<T> resources) {
    RedisPaging<T> paging = new RedisPaging<>();
    paging.total = total < 0 ? (resources != null ? resources.size() : 0) : total;
    paging.index = index < 0 ? 0 : index;
    paging.count = count < 0 ? 0 : count;
    paging.resources = resources == null ? Collections.emptyList() : resources;
    return paging;
  }

  static <T> Paging<T> ofEmpty() {
    RedisPaging<T> paging = new RedisPaging<>();
    paging.total = 0;
    paging.index = 0;
    paging.count = 0;
    paging.resources = Collections.emptyList();
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
