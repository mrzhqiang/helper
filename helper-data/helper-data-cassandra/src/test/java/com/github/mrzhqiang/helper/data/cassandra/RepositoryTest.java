package com.github.mrzhqiang.helper.data.cassandra;

import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

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
        entity.setId(UUID.randomUUID());
        entity.setName("测试实体");

        repository.save(entity);
    }

    @After
    public void tearDown() {
        repository.delete(entity);
        repository = null;
    }

    @Test
    public void findById() {
        Optional<CassandraTest.TestTable> optionalTable = repository.findById(entity.getId(), entity.getName());
        Assert.assertTrue(optionalTable.isPresent());
        Assert.assertEquals(entity, optionalTable.get());
    }

    @Test
    public void list() {
        for (int i = 0; i < 100; i++) {
            CassandraTest.TestTable entity = new CassandraTest.TestTable();
            entity.setId(UUID.randomUUID());
            entity.setName("测试实体" + i);
            repository.save(entity);
        }

        repository.findAll().forEach(testTable -> {
            System.out.println(testTable);
            repository.delete(testTable);
        });
    }

    @ImplementedBy(SimpleCassandraTestRepository.class)
    private interface TestRepository extends CassandraRepository<CassandraTest.TestTable> {
    }

    @Singleton
    private static final class SimpleCassandraTestRepository
            extends SimpleCassandraRepository<CassandraTest.TestTable> implements TestRepository {

        @Inject
        private SimpleCassandraTestRepository(Cassandra cassandra) {
            super(cassandra.mapper(CassandraTest.TestTable.class));
            cassandra.execute(session -> session.execute(CassandraTest.TestKeyspace.CREATE));
            cassandra.execute(session -> session.execute(new CassandraTest.TestTable().createCQL()));
        }
    }
}