package helper.data.redis;

import com.google.common.base.MoreObjects;
import java.util.Date;
import lombok.Data;

/**
 * Redis 实体。
 * <p>
 * 鉴于 Redis 存储的基本是字符串，这里需要有一个实体抽象进行转换。
 *
 * @author mrzhqiang
 */
@Data
public abstract class RedisEntity {
  protected String id;
  protected Date created;
  protected Date modified;

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
}
