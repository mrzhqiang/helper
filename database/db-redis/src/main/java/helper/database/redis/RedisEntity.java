package helper.database.redis;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import helper.database.Entity;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Redis 实体。
 * <p>
 * 鉴于 Redis 存储的基本是字符串，这里需要有一个实体抽象进行转换。
 *
 * @author mrzhqiang
 */
public abstract class RedisEntity implements Entity {
  public static final Gson GSON = new GsonBuilder()
      .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
      .setPrettyPrinting()
      .create();
  private static final Type MAP_TYPE = new TypeToken<HashMap<String, String>>() {
  }.getType();

  transient private String id;
  Date created;
  Date updated;

  public void setId(Object id) {
    Preconditions.checkNotNull(id);
    this.id = String.valueOf(id);
  }

  /**
   * 获得主键。
   * <p>
   * 如果实体是由系统创建的，那么主键不存在，则返回 Null 值。
   *
   * @return 主键。不存在数据库中，则为 Null。
   */
  @Override
  public Object primaryKey() {
    return id;
  }

  /**
   * 获得内容。
   * <p>
   * 通过 Json 序列化对象为字符串，再解析为 Map 类型。
   *
   * @return 内容。
   */
  Map<String, String> contentValue() {
    return GSON.fromJson(GSON.toJson(this), MAP_TYPE);
  }

  /**
   * toString 的辅助方法。
   *
   * @return Guava 的 toString 辅助类。
   */
  protected MoreObjects.ToStringHelper toStringHelper() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("created", created)
        .add("updated", updated);
  }

  @Override public int hashCode() {
    return Objects.hash(id, created, updated);
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
        && Objects.equals(this.updated, other.updated);
  }
}
