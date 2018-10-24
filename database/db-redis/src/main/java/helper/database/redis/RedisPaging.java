package helper.database.redis;

import com.google.common.base.MoreObjects;
import helper.database.Paging;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

/**
 * 基于 Redis 客户端的分页。
 *
 * @author mrzhqiang
 */
public final class RedisPaging<T extends RedisEntity> implements Paging<T> {
  static <T extends RedisEntity> Paging<T> of(long total, int index, int count,
      @Nullable List<T> resources) {
    RedisPaging<T> paging = new RedisPaging<>();
    paging.total = total < 0 ? (resources != null ? resources.size() : 0) : total;
    paging.index = index < 0 ? 0 : index;
    paging.count = count < 0 ? 0 : count;
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
