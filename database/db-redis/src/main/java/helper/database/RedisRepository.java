package helper.database;

import com.google.common.collect.Lists;
import helper.database.internal.Util;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;

/**
 * 基于 Redis 的仓库。
 * <p>
 * Redis 作为数据库也是可行的。
 *
 * @author mrzhqiang
 */
public abstract class RedisRepository<E extends Entity> implements Repository<E> {
  /**
   * Redis 客户端。
   * <p>
   * 通常是由实现类提供的单例对象。
   *
   * @return Redis 客户端。
   */
  abstract Redis redis();

  /**
   * 通过主键生成对应的 Key。
   * <p>
   * 如果主键不存在，则生成全新的主键，以备 save 方法插入数据。
   *
   * @param primaryKey 主键。
   * @return 字符串。Redis Key。
   */
  abstract String key(Object primaryKey);

  /**
   * 将主键和内容转换为实体。
   *
   * @param primaryKey 主键。
   * @param value 内容。
   * @return 实体对象。
   */
  abstract E transform(Object primaryKey, Map<String, String> value);

  @Override public void save(E entity) {
    redis().execute(jedis -> jedis.hmset(key(entity.primaryKey()), entity.contentValue()));
  }

  @Override public void delete(Object... primaryKeys) {
    redis().execute(jedis -> jedis.del(key(primaryKeys[0])));
  }

  @Override public Optional<E> get(Object... primaryKeys) {
    return redis().find(jedis -> transform(primaryKeys[0], jedis.hgetAll(key(primaryKeys[0]))));
  }

  @Override public Paging<E> page(int index, int size, @Nullable Map<String, Object> clause) {
    int start = Util.computeFirstRow(index, size) - 1;
    int maxRows = Util.computeMaxRows(size);
    int end = start + maxRows - 1;

    if (clause == null) {
      String key = key(":all");
    }
    return RedisPaging.ofEmpty();
  }

  @Override public List<E> list(@Nullable Map<String, Object> clause) {
    return null;
  }
}
