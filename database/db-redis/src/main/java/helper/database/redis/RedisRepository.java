package helper.database.redis;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import helper.database.Paging;
import helper.database.Repository;
import helper.database.internal.Util;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

/**
 * 基于 Redis 的仓库。
 * <p>
 * Redis 作为持久化数据库也是可行的。
 *
 * @author mrzhqiang
 */
public abstract class RedisRepository<E extends RedisEntity> implements Repository<E> {
  private static final Logger LOGGER = LoggerFactory.getLogger("redis");

  /**
   * 条件子句匹配键值。
   * <p>
   * 关于格式，请参考：
   * <pre>
   *   http://doc.redisfans.com/key/keys.html
   * </pre>
   */
  public static final String KEY_CLAUSE_MATCH = "match";
  /**
   * 在条件子句中，匹配所有字符串。
   */
  public static final String PATTERN_ALL = "*";
  /**
   * 在条件子句中，匹配任意一个字符。
   */
  public static final String PATTERN_HOLDER = "?";
  /**
   * 在条件子句中，匹配区间内的任意一个字符。
   */
  public static final String PATTERN_PREFIX = "[";
  /**
   * 在条件子句中，匹配区间内的任意一个字符。
   */
  public static final String PATTERN_SUFFIX = "]";
  /**
   * 查询全部的数据，所使用的键值名。
   */
  protected static final String KEY_ALL = "all";

  private static final String KEY_NEXT_ID = "nextId";
  private static final int DEFAULT_SCAN_PARAM_COUNT = 2000;
  private static final Gson GSON = new GsonBuilder()
      .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
      .setPrettyPrinting()
      .create();
  private static final Type TYPE_HASH_MAP = new TypeToken<HashMap<String, String>>() {
  }.getType();

  private final Class<E> entityClass;

  /**
   * 由实体类型构造的 Redis 仓库。
   *
   * @param entityClass 实体类型。
   */
  protected RedisRepository(Class<E> entityClass) {
    Preconditions.checkNotNull(entityClass);

    this.entityClass = entityClass;
  }

  /**
   * Redis 客户端。
   * <p>
   * 通常是由实现类提供的单例对象。
   *
   * @return Redis 客户端。
   */
  abstract protected Redis redis();

  /**
   * 仓库的秘钥空间。
   *
   * @return 秘钥空间。即 Key 的前缀。
   */
  abstract protected String keyspace();

  /**
   * 生成对应的 Key。
   *
   * @param tables 表格数组。
   * @return Redis 的 Key 值。
   */
  protected String key(Object... tables) {
    StringBuilder builder = new StringBuilder(keyspace());
    for (Object table : tables) {
      builder.append(table);
    }
    return builder.toString();
  }

  /**
   * 将 Redis 中的哈希表内容转换为实体。
   *
   * @param contentValue 哈希表内容。
   * @return 实体对象。
   */
  protected @Nullable E transform(Map<String, String> contentValue) {
    Preconditions.checkNotNull(contentValue);
    if (contentValue.isEmpty()) {
      return null;
    }
    return Util.create(() -> {
      E entity = GSON.fromJson(GSON.toJson(contentValue), entityClass);
      if (Strings.isNullOrEmpty(entity.id)) {
        return null;
      }
      return entity;
    });
  }

  private Map<String, String> contentValue(E entity) {
    Preconditions.checkNotNull(entity);
    return Util.create(() -> GSON.fromJson(GSON.toJson(entity), TYPE_HASH_MAP));
  }

  private ScanParams ofParams(@Nullable Map<String, Object> clause, int size) {
    ScanParams params = new ScanParams();
    if (clause != null) {
      Object match = clause.get(KEY_CLAUSE_MATCH);
      if (match instanceof String) {
        params.match((String) match);
      }
    }
    if (size <= 0) {
      size = DEFAULT_SCAN_PARAM_COUNT;
    }
    params.count(size);
    return params;
  }

  @Override public void save(E entity) {
    Preconditions.checkNotNull(entity);

    LOGGER.debug("Save entity: {}", entity);
    Object id = entity.id;
    if (id == null) {
      id = redis().find(jedis -> jedis.incr(key(KEY_NEXT_ID))).orElse(null);
      LOGGER.debug("Create entity id: {}", id);
      entity.id = String.valueOf(id);
      entity.created = new Date();
    }
    entity.modified = new Date();
    // 需要监视的键，以防止期间有其他改动
    String key = key(id);
    redis().multi(transaction -> {
      transaction.hmset(key, contentValue(entity));
      return transaction.zadd(key(KEY_ALL), entity.modified.getTime(), entity.id);
    }, key).ifPresent(
        response -> LOGGER.debug("Save entity {} status {}", entity, response.get() > 0));
  }

  @Override public void delete(Object... primaryKeys) {
    Preconditions.checkNotNull(primaryKeys);
    Preconditions.checkArgument(primaryKeys.length > 0);

    LOGGER.debug("Delete primary key: {}", Arrays.toString(primaryKeys));
    redis().pipelined(pipeline -> {
      // 通常 Key 只取第一个，除非是 CQL 数据库，由于分区键的存在，必须设定主键数组
      String primaryKey = String.valueOf(primaryKeys[0]);
      pipeline.del(key(primaryKey));
      pipeline.zrem(key(KEY_ALL), primaryKey);
    });
  }

  @Override public Optional<E> get(Object... primaryKeys) {
    Preconditions.checkNotNull(primaryKeys);
    Preconditions.checkArgument(primaryKeys.length > 0);

    LOGGER.debug("Get primary key: {}", Arrays.toString(primaryKeys));
    if (primaryKeys[0] != null) {
      String primaryKey = String.valueOf(primaryKeys[0]);
      return redis().find(jedis -> transform(jedis.hgetAll(key(primaryKey))));
    }
    return Optional.empty();
  }

  @Override public Paging<E> list(int index, int size, @Nullable Map<String, Object> clause) {
    LOGGER.debug("List index: {} size: {} clause: {}", index, size, clause);
    String key = key(KEY_ALL);
    long total = redis().find(jedis -> jedis.zcard(key)).orElse(-1L);
    if (total <= 0) {
      return Paging.ofEmpty(index);
    }

    int maxRows = Paging.computeMaxRows(size);
    int count = Paging.computePageCount(total, maxRows);
    List<E> resources;
    if (clause == null) {
      int start = Paging.computeFirstRow(index, maxRows) - 1;
      int end = (start + maxRows) - 1;
      resources = redis().find(jedis ->
          jedis.zrevrange(key, start, end)
              .stream()
              .map(s -> transform(jedis.hgetAll(key(s))))
              .collect(Collectors.toList()))
          .orElse(Collections.emptyList());
    } else {
      ScanParams scanParams = ofParams(clause, maxRows);
      resources = redis().find(jedis -> {
        // 注意：这里扫描到的是从小到大的序列，如果不符合期望，则 clause 应该传递 Null 值
        ScanResult<Tuple> scanResult = jedis.zscan(key, SCAN_POINTER_START, scanParams);
        for (int i = 1; i < index; i++) {
          scanResult = jedis.zscan(key, scanResult.getStringCursor(), scanParams);
        }
        return scanResult.getResult()
            .stream()
            .map(Tuple::getElement)
            .map(s -> transform(jedis.hgetAll(key(s))))
            .collect(Collectors.toList());
      }).orElse(Collections.emptyList());
    }
    return Paging.of(total, index, count, resources);
  }
}
