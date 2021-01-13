package com.github.mrzhqiang.helper.data.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.mrzhqiang.helper.data.repository.PagingRepository;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import redis.clients.jedis.ScanParams;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于 Redis 的仓库。
 * <p>
 * Redis 作为持久化数据库也是可行的。
 *
 * @author mrzhqiang
 */
public interface RedisRepository<T> extends PagingRepository<T, String> {

    /**
     * 条件子句匹配键值。
     * <p>
     * 关于格式，请参考：
     * <pre>
     *   http://doc.redisfans.com/key/keys.html
     * </pre>
     */
    String KEY_CLAUSE_MATCH = "match";
    /**
     * 在条件子句中，匹配所有字符串。
     */
    String PATTERN_ALL = "*";
    /**
     * 在条件子句中，匹配任意一个字符。
     */
    String PATTERN_HOLDER = "?";
    /**
     * 在条件子句中，匹配区间内的任意一个字符。
     */
    String PATTERN_PREFIX = "[";
    /**
     * 在条件子句中，匹配区间内的任意一个字符。
     */
    String PATTERN_SUFFIX = "]";
    /**
     * 查询全部的数据，所使用的键值名。
     */
    String KEY_ALL = "all";

    String KEY_NEXT_ID = "nextId";

    String PLACEHOLDER = ":";

    TypeReference<Map<String, String>> MAP_TYPE_REFERENCE = new TypeReference<Map<String, String>>() {
    };

    /**
     * 生成对应的 Key。
     *
     * @return Redis 的 Key 值。
     */
    String key(String id);

    /**
     * 将 Redis 中的哈希表内容转换为实体。
     *
     * @param contentValue 哈希表内容。
     * @return 实体对象。
     */
    T transform(Map<String, String> contentValue);

    @Override
    default List<T> findAll() {
        return findAll(Collections.emptyMap());
    }

    /**
     * 通过查询条件检索全量数据。
     *
     * @return 包含查询到的全部数据。
     */
    List<T> findAll(@Nullable Map<String, Object> where);

    @Override
    default boolean existsById(String key) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(key), "key == null or empty");

        return findById(key).isPresent();
    }

    @Override
    default List<T> findAllById(Iterable<String> keys) {
        Preconditions.checkArgument(Objects.nonNull(keys), "keys == null");

        return Lists.newArrayList(keys).stream()
                .map(this::key)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @Override
    default List<T> saveAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        List<T> results = Lists.newArrayList();
        entities.forEach(it -> Optional.ofNullable(save(it)).ifPresent(results::add));
        return results;
    }

    static ScanParams ofParams(@Nullable Map<String, Object> clause, int size) {
        ScanParams params = new ScanParams();
        if (Objects.nonNull(clause) && !clause.isEmpty()) {
            Object match = clause.get(KEY_CLAUSE_MATCH);
            if (match instanceof String) {
                params.match((String) match);
            }
        }
        params.count(size);

        return params;
    }

    static <T> Map<String, String> contentValue(T entity) {
        Preconditions.checkNotNull(entity, "entity == null");

        return Datas.apply(entity, it -> Jsons.mapper().convertValue(it, MAP_TYPE_REFERENCE));
    }
}
