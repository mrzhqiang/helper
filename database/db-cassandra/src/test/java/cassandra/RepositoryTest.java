package cassandra;

import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import helper.database.Repository;
import helper.database.cassandra.Cassandra;
import helper.database.cassandra.CassandraRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mrzhqiang
 */
public class RepositoryTest {
  private TestRepository repository;
  private CassandraTest.TestTable entity;

  @Before
  public void setUp() {
    repository = Guice.createInjector().getInstance(TestRepository.class);

    entity = new CassandraTest.TestTable();
    entity.id = UUID.randomUUID();
    entity.name = "测试实体";

    repository.save(entity);
  }

  @After
  public void tearDown() {
    repository.delete(entity.id, entity.name);
    repository = null;
  }

  @Test
  public void get() {
    Optional<CassandraTest.TestTable> optionalTable = repository.get(entity.id, entity.name);
    Assert.assertTrue(optionalTable.isPresent());
    Assert.assertEquals(entity, optionalTable.get());
  }

  @Test
  public void list() {
    for (int i = 0; i < 100; i++) {
      CassandraTest.TestTable entity = new CassandraTest.TestTable();
      entity.id = UUID.randomUUID();
      entity.name = "测试实体" + i;
      repository.save(entity);
    }

    repository.list(2, 30, null)
        .resources.forEach(testTable -> {
      System.out.println(testTable);
      repository.delete(testTable.id, testTable.name);
    });
  }

  @ImplementedBy(CassandraTestRepository.class)
  private interface TestRepository extends Repository<CassandraTest.TestTable> {
  }

  @Singleton
  private static final class CassandraTestRepository
      extends CassandraRepository<CassandraTest.TestTable> implements TestRepository {

    private final Cassandra cassandra;

    @Inject
    private CassandraTestRepository(Cassandra cassandra) {
      super(CassandraTest.TestTable.class);
      this.cassandra = cassandra;
      this.cassandra.execute(session -> session.execute(CassandraTest.TestKeyspace.CREATE));
      cassandra.execute(session -> session.execute(new CassandraTest.TestTable().createCQL()));
    }

    @Override protected Cassandra cassandra() {
      return cassandra;
    }
  }
}