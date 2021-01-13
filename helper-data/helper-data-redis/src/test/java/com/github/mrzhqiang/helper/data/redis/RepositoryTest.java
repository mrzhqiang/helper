package com.github.mrzhqiang.helper.data.redis;

import com.github.mrzhqiang.helper.data.DataAccessException;
import com.github.mrzhqiang.helper.data.domain.Page;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author mrzhqiang
 */
public final class RepositoryTest {

    private TestRepository repository;

    @Before
    public void setUp() {
        repository = Guice.createInjector().getInstance(TestRepository.class);
    }

    @After
    public void tearDown() {
        repository = null;
    }

    @Test
    public void testEntity() {
        TestEntity entity = new TestEntity();
        entity.name = "测试实体";
        repository.save(entity);

        Optional<TestEntity> optionalEntity = repository.findById(entity.getId());
        assertTrue(optionalEntity.isPresent());
        assertEquals(entity, optionalEntity.get());

        List<TestEntity> search = repository.search(0, System.currentTimeMillis());
        search.forEach(testEntity -> {
            LoggerFactory.getLogger("redis").info("delete：" + testEntity);
            repository.deleteById(testEntity.getId());
        });
    }

    @Test
    public void testDatabaseException() {
        try {
            repository.deleteById("null");
        } catch (DataAccessException e) {
            LoggerFactory.getLogger("database").info("error successful!", e);
        }
    }

    @Test
    public void testPaging() {
        for (int i = 0; i < 100; i++) {
            TestEntity entity = new TestEntity();
            entity.name = "测试实体" + i;
            repository.save(entity);
        }

        Page<TestEntity> page = repository.findAll(RedisPageRequest.of(0, 20));
        System.out.println(page);

        for (int i = 0; i < 100; i++) {
            repository.search(0, System.currentTimeMillis())
                    .forEach(entity -> repository.deleteById(entity.getId()));
        }
    }

    @Keyspace("helper:db-redis:test")
    @Data
    @EqualsAndHashCode(callSuper = true)
    private static final class TestEntity extends RedisEntity {
        String name;
    }

    @ImplementedBy(TestRedisRepository.class)
    private interface TestRepository extends RedisRepository<TestEntity> {
        List<TestEntity> search(long startTime, long endTime);
    }

    @Singleton
    private static final class TestRedisRepository extends SimpleRedisRepository<TestEntity>
            implements TestRepository {
        private final Redis redis;

        @Inject
        private TestRedisRepository(Redis redis) {
            super(TestEntity.class);
            this.redis = redis;
        }

        @Override
        public Redis redis() {
            return redis;
        }

        @Override
        public List<TestEntity> search(long startTime, long endTime) {
            return redis().find(jedis ->
                    jedis.zrevrangeWithScores(key(KEY_ALL), startTime, endTime)
                            .stream()
                            .map(Tuple::getElement)
                            .map(s -> transform(jedis.hgetAll(key(s))))
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList())
            ).orElse(Collections.emptyList());
        }
    }
}
