package helper.database.redis.entity;

import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import helper.database.redis.RedisEntity;
import helper.database.redis.Redis;
import helper.database.redis.RedisRepository;
import helper.database.Repository;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.*;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Tuple;

import static org.junit.Assert.*;

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

    Optional<TestEntity> optionalEntity = repository.get(entity.primaryKey());
    assertTrue(optionalEntity.isPresent());
    assertEquals(entity, optionalEntity.get());

    List<TestEntity> search = repository.search(0, System.currentTimeMillis());
    search.forEach(testEntity -> {
      LoggerFactory.getLogger("redis").info("delete：" + testEntity);
      repository.delete(testEntity.primaryKey());
    });
  }

  private static final class TestEntity extends RedisEntity {
    String name;

    @Override public int hashCode() {
      return Objects.hash(super.hashCode(), name);
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      if (!(obj instanceof TestEntity)) {
        return false;
      }

      TestEntity other = ((TestEntity) obj);
      return super.equals(other)
          && Objects.equals(this.name, other.name);
    }

    @Override public String toString() {
      return toStringHelper()
          .add("name", name)
          .toString();
    }
  }

  @ImplementedBy(TestRedisRepository.class)
  private interface TestRepository extends Repository<TestEntity> {
    List<TestEntity> search(long startTime, long endTime);
  }

  @Singleton
  private static final class TestRedisRepository extends RedisRepository<TestEntity>
      implements TestRepository {
    private final Redis redis;

    @Inject
    private TestRedisRepository(Redis redis) {
      super(TestEntity.class);
      this.redis = redis;
    }

    @Override protected Redis redis() {
      return redis;
    }

    @Override protected String keyPrefix() {
      return "helper:db-redis:test:";
    }

    @Override public List<TestEntity> search(long startTime, long endTime) {
      return redis().find(jedis ->
          jedis.zrevrangeWithScores(key(KEY_ALL), startTime, endTime)
              .stream()
              .map(Tuple::getElement)
              .map(s -> transform(jedis.hgetAll(key(s))))
              .collect(Collectors.toList())
      ).orElse(Collections.emptyList());
    }
  }
}
