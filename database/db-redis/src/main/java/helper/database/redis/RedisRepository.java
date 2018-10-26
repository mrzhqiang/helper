package helper.database.redis;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import helper.database.Entity;
import helper.database.Paging;
import helper.database.Repository;
import helper.database.internal.Util;
import java.lang.reflect.Type;
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
public abstract class RedisRepository<E extends Entity> implements Repository<E> {
  private static final Logger LOGGER = LoggerFactory.getLogger("redis");

  private static final int DEFAULT_SCAN_PARAM_COUNT = 2000;
  private static final Gson GSON = new GsonBuilder()
      .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
      .setPrettyPrinting()
      .create();
  private static final Type TYPE_HASH_MAP = new TypeToken<HashMap<String, String>>() {
  }.getType();

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
  public static final String KEY_ALL = "all";
  /**
   * 查询下一个 ID，所使用的键值名。
   */
  public static final String KEY_NEXT_ID = "nextId";

  private final Class<E> entityClass;

  /**
   * 由实体类型构造的 Redis 仓库。
   *
   * @param entityClass 实体类型。
   */
  protected RedisRepository(Class<E> entityClass) {
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
   * 仓库的键名前缀。
   *
   * @return 字符串。
   */
  abstract protected String keyPrefix();

  /**
   * 通过主键生成对应的 Key。
   * <p>
   * 如果主键不存在，则生成全新的主键，以备 save 方法插入数据。
   *
   * @param primaryKey 主键。通常是字符串，这里为了兼容 Repository 接口而设计为 Object。非 Null。
   * @return 字符串。Redis 的 Key 值。
   */
  protected String key(Object primaryKey) {
    return keyPrefix() + primaryKey;
  }

  /**
   * 将 Redis 中的哈希表内容转换为实体。
   *
   * @param contentValue 哈希表内容。
   * @return 实体对象。
   */
  protected E transform(Map<String, String> contentValue) {
    return Util.create(() -> GSON.fromJson(GSON.toJson(contentValue), entityClass));
  }

  private Map<String, String> contentValue(E entity) {
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
    Object primaryKey = entity.primaryKey();
    if (primaryKey == null) {
      primaryKey = redis().find(jedis -> jedis.incr(key(KEY_NEXT_ID))).orElse(null);
      entity.setPrimaryKey(primaryKey);
      entity.setCreated(new Date());
    }
    // 需要监视的键，以防止期间有其他改动
    String key = key(entity.primaryKey());
    redis().multi(transaction -> {
      entity.setModified(new Date());
      transaction.hmset(key, contentValue(entity));
      long score = entity.modified().toEpochMilli();
      String member = String.valueOf(entity.primaryKey());
      return transaction.zadd(key(KEY_ALL), score, member);
    }, key).ifPresent(response ->
        LOGGER.info("Save entity {} status {}", entity, response.get() > 0));
  }

  @Override public void delete(Object... primaryKeys) {
    redis().pipelined(pipeline -> {
      // 通常 Key 只取第一个，除非是 CQL 数据库，由于分区键的存在，必须设定主键数组
      String primaryKey = String.valueOf(primaryKeys[0]);
      pipeline.del(key(primaryKey));
      pipeline.zrem(key(KEY_ALL), primaryKey);
    });
  }

  @Override public Optional<E> get(Object... primaryKeys) {
    String primaryKey = String.valueOf(primaryKeys[0]);
    return redis().find(jedis -> transform(jedis.hgetAll(key(primaryKey))));
  }

  @Override public Paging<E> page(int index, int size, @Nullable Map<String, Object> clause) {
    String key = key(KEY_ALL);
    int maxRows = Paging.computeMaxRows(size);
    long total = redis().find(jedis -> jedis.zcard(key)).orElse(-1L);
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
    return RedisPaging.of(total, index, count, resources);
  }

  @Override public List<E> list(@Nullable Map<String, Object> clause) {
    // 只列出前 10 个实体，不保证顺序
    return redis().find(jedis ->
        jedis.zscan(key(KEY_ALL), SCAN_POINTER_START, ofParams(clause, 10)).getResult()
            .stream()
            .map(Tuple::getElement)
            .map(s -> transform(jedis.hgetAll(key(s))))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
