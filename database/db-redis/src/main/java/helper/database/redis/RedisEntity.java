package helper.database.redis;

import com.google.common.base.MoreObjects;
import java.util.Date;
import java.util.Objects;

/**
 * Redis 实体。
 * <p>
 * 鉴于 Redis 存储的基本是字符串，这里需要有一个实体抽象进行转换。
 *
 * @author mrzhqiang
 */
public abstract class RedisEntity {
  public String id;
  public Date created;
  public Date modified;

  /**
   * toString 的辅助方法。
   *
   * @return Guava 的 toString 辅助类。
   */
  protected MoreObjects.ToStringHelper toStringHelper() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("created", created)
        .add("modified", modified);
  }

  @Override public int hashCode() {
    return Objects.hash(id, created, modified);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof RedisEntity)) {
      return false;
    }

    RedisEntity other = ((RedisEntity) obj);
    return Objects.equals(this.id, other.id)
        && Objects.equals(this.created, other.created)
        && Objects.equals(this.modified, other.modified);
  }
}
