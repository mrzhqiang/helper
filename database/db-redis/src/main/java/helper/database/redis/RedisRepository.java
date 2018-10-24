package helper.database.redis;

import helper.database.Paging;
import helper.database.Repository;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

/**
 * 基于 Redis 的仓库。
 * <p>
 * Redis 作为数据库也是可行的。
 *
 * @author mrzhqiang
 */
public abstract class RedisRepository<E extends RedisEntity> implements Repository<E> {
  private static final int DEFAULT_SCAN_PARAM_COUNT = 2000;

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
   * @param primaryKey 主键。通常是字符串，这里为了兼容 Repository 接口而设计为 Object。
   * @return 字符串。Redis 的 Key 值。
   */
  protected String key(Object primaryKey) {
    return keyPrefix() + primaryKey;
  }

  /**
   * 将主键和内容转换为实体。
   *
   * @param primaryKey 主键。
   * @param contentValue 内容。
   * @return 实体对象。
   */
  abstract protected E transform(Object primaryKey, Map<String, String> contentValue);

  /**
   * 将条件子句和页面大小转换为扫描参数。
   *
   * @param clause 条件子句。
   * @param size 页面大小。
   * @return 扫描参数。
   */
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
    if (entity.primaryKey() == null) {
      entity.setId(redis().find(jedis -> jedis.incr(key(KEY_NEXT_ID))).orElse(null));
      entity.created = new Date();
    }
    entity.updated = new Date();
    redis().pipelined(pipeline -> {
      pipeline.hmset(key(entity.primaryKey()), entity.contentValue());
      pipeline.zadd(key(KEY_ALL), entity.updated.getTime(), String.valueOf(entity.primaryKey()));
    });
  }

  @Override public void delete(Object... primaryKeys) {
    // 通常 Key 只取第一个，除非是 CQL 数据库，由于分区键的存在，必须设定主键数组
    String primaryKey = String.valueOf(primaryKeys[0]);
    redis().pipelined(pipeline -> {
      pipeline.del(key(primaryKey));
      pipeline.zrem(key(KEY_ALL), primaryKey);
    });
  }

  @Override public Optional<E> get(Object... primaryKeys) {
    String primaryKey = String.valueOf(primaryKeys[0]);
    return redis().find(jedis -> transform(primaryKey, jedis.hgetAll(key(primaryKey))));
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
              .map(s -> transform(s, jedis.hgetAll(key(s))))
              .collect(Collectors.toList()))
          .orElse(Collections.emptyList());
    } else {
      ScanParams scanParams = ofParams(clause, maxRows);
      resources = redis().find(jedis -> {
        // 注意：这里扫描到的是从小到大的序列，如果不符合期望，则 clause 应该传递 Null 值。
        ScanResult<Tuple> scanResult = jedis.zscan(key, SCAN_POINTER_START, scanParams);
        for (int i = 1; i < index; i++) {
          scanResult = jedis.zscan(key, scanResult.getStringCursor(), scanParams);
        }
        return scanResult.getResult()
            .stream()
            .map(Tuple::getElement)
            .map(s -> transform(s, jedis.hgetAll(key(s))))
            .collect(Collectors.toList());
      }).orElse(Collections.emptyList());
    }
    return RedisPaging.of(total, index, count, resources);
  }

  @Override public List<E> list(@Nullable Map<String, Object> clause) {
    String key = key(KEY_ALL);
    return redis().find(jedis ->
        jedis.zscan(key, SCAN_POINTER_START, ofParams(clause, 10)).getResult()
            .stream()
            .map(Tuple::getElement)
            .map(s -> transform(s, jedis.hgetAll(key(s))))
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
