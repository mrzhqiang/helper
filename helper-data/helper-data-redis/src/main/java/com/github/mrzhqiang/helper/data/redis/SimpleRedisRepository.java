package com.github.mrzhqiang.helper.data.redis;

import com.github.mrzhqiang.helper.data.domain.Page;
import com.github.mrzhqiang.helper.data.domain.PageRequest;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.github.mrzhqiang.helper.data.domain.Paging;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Response;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.Tuple;

import javax.annotation.Nullable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

@Slf4j(topic = "helper.data.redis")
public abstract class SimpleRedisRepository<T extends RedisEntity> implements RedisRepository<T> {

    private final Class<T> entityClass;
    private final String keyspace;

    /**
     * 由实体类型构造的 Redis 仓库。
     *
     * @param entityClass 实体类型。
     */
    protected SimpleRedisRepository(Class<T> entityClass) {
        Preconditions.checkNotNull(entityClass, "entity class == null");
        Keyspace keyspace = entityClass.getAnnotation(Keyspace.class);
        Preconditions.checkArgument(Objects.nonNull(keyspace), "entity must be has @Keyspace");

        this.entityClass = entityClass;
        this.keyspace = keyspace.value();
    }

    public abstract Redis redis();

    @Override
    public String key(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id == null or empty");

        return String.join(PLACEHOLDER, keyspace, id);
    }

    @Override
    public Optional<T> findById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id == null or empty");

        String key = key(id);
        log.debug("Find by key: {}", key);
        return redis().find(jedis -> transform(jedis.hgetAll(key)));
    }

    @Override
    public List<T> findAll(@Nullable Map<String, Object> where) {
        long count = this.count();
        if (count <= 0) {
            return Collections.emptyList();
        }

        String key = key(KEY_ALL);
        log.debug("Find all key: {}", key);
        ScanParams params = RedisRepository.ofParams(where, Integer.MAX_VALUE);
        return redis().find(jedis -> {
            // 注意：这里扫描到的是从小到大的序列，如果不符合期望，则 clause 应该传递 Null 值
            ScanResult<Tuple> scanResult = jedis.zscan(key, SCAN_POINTER_START, params);
            return scanResult.getResult()
                    .stream()
                    .map(Tuple::getElement)
                    .map(it -> transform(jedis.hgetAll(key(it))))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }).orElse(Collections.emptyList());
    }

    @Override
    public long count() {
        String key = key(KEY_ALL);
        return redis().find(jedis -> jedis.zcard(key)).orElse(-1L);
    }

    @Override
    public void deleteById(String id) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "id == null or empty");

        String key = key(id);
        String keys = key(KEY_ALL);
        log.debug("delete by key: {}", key);

        redis().pipelined(pipeline -> {
            pipeline.del(key);
            pipeline.zrem(keys, id);
        });
    }

    @Override
    public void delete(T entity) {
        Preconditions.checkArgument(Objects.nonNull(entity), "entity == null");

        String id = entity.getId();
        Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "entity.id == null or empty");

        deleteById(id);
    }

    @Override
    public void deleteAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        String keys = key(KEY_ALL);
        List<String> removes = Lists.newArrayList();
        entities.forEach(entity -> Optional.ofNullable(entity)
                .map(it -> it.id)
                .filter(id -> !id.isEmpty())
//                .map(this::key)
                .ifPresent(removes::add));
        log.debug("delete all by key: {}", removes);

        redis().pipelined(pipeline -> {
            pipeline.zrem(keys, removes.toArray(new String[0]));
            pipeline.del(removes.stream().map(this::key).toArray(String[]::new));
        });
    }

    @Override
    public void deleteAll() {
        deleteAll(findAll());
    }

    @Override
    public Page<T> findAll(Pageable pageable, @Nullable Map<String, Object> where) {
        Preconditions.checkArgument(Objects.nonNull(pageable), "pageable == null");

        if (pageable.isUnpaged()) {
            List<T> results = Objects.isNull(where) ? findAll() : findAll(where);
            return Paging.of(results);
        }

        String key = key(KEY_ALL);
        long total = redis().find(jedis -> jedis.zcard(key)).orElse(-1L);
        if (total <= 0) {
            return Page.empty(pageable);
        }

        RedisPageRequest.validatePageable(pageable);

        if (log.isDebugEnabled()) {
            log.debug("find all by pageable: {}, and where: {}", pageable, where);
        }

        return redis().find(jedis -> {
            String cursor = SCAN_POINTER_START;
            if (pageable instanceof RedisPageRequest) {
                cursor = MoreObjects.firstNonNull(((RedisPageRequest) pageable).getCursor(), SCAN_POINTER_START);
            }
            int pageSize = pageable.getPageSize();
            ScanParams params = RedisRepository.ofParams(where, pageSize);
            ScanResult<Tuple> scanResult = jedis.zscan(key, cursor, params);
            // 有 pagingState 基本上不需要设置 page （页面编号），这里参考 Spring Boot Data 设置为 0
            RedisPageRequest pageRequest = RedisPageRequest.of(
                    PageRequest.of(0, pageSize), scanResult.getCursor());

            if (log.isDebugEnabled()) {
                log.debug("generate: {}", pageRequest);
            }

            List<T> content = scanResult.getResult()
                    .stream()
                    .map(Tuple::getElement)
                    .map(s -> transform(jedis.hgetAll(key(s))))
                    .collect(Collectors.toList());

            if (log.isDebugEnabled()) {
                log.debug("slice find all size: {}, page size: {}", content.size(), pageSize);
            }

            return Paging.of(content, pageRequest, total);
        }).orElse(Paging.ofEmpty());
    }

    @Nullable
    @Override
    public T transform(Map<String, String> contentValue) {
        Preconditions.checkNotNull(contentValue, "content value == null");

        if (contentValue.isEmpty()) {
            return null;
        }

        return Datas.apply(contentValue, it -> Jsons.fromJson(Jsons.toJson(it), entityClass));
    }

    @Nullable
    @Override
    public T save(T entity) {
        Preconditions.checkNotNull(entity, "entity == null");

        log.debug("Save entity: {}", entity);
        String id = entity.id;
        if (Strings.isNullOrEmpty(id)) {
            Optional<Long> nextId = redis().find(jedis -> jedis.incr(key(KEY_NEXT_ID)));
            if (!nextId.isPresent()) {
                return null;
            }
            id = String.valueOf(nextId.get());
            log.debug("Next entity id: {}", id);
            entity.id = id;
            entity.created = Instant.now();
        }
        entity.modified = Instant.now();
        // 需要监视的键，以防止期间有其他改动
        String key = key(id);
        Optional<Response<Long>> multi = redis().multi(transaction -> {
            transaction.hmset(key, RedisRepository.contentValue(entity));
            return transaction.zadd(key(KEY_ALL), entity.modified.toEpochMilli(), entity.id);
        }, key);

        multi.ifPresent(it -> log.debug("Save entity {} status {}", entity, it.get() > 0));
        return entity;
    }
}
