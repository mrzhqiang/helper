package cassandra;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Guice;
import helper.database.cassandra.Cassandra;
import helper.database.cassandra.CassandraEntity;
import java.util.Objects;
import java.util.UUID;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author mrzhqiang
 */
public class CassandraTest {

  private Cassandra cassandra;

  @Before
  public void setUp() {
    cassandra = Guice.createInjector().getInstance(Cassandra.class);
  }

  @After
  public void tearDown() {
    cassandra = null;
  }

  @Test
  public void execute() {
    cassandra.execute(session -> {
          ResultSet resultSet = session.execute(
              QueryBuilder.select("cluster_name", "cql_version", "release_version")
                  .from("system", "local"));
          System.out.println(resultSet.one());
        }
    );
  }

  @Test
  public void mapper() {
    cassandra.execute(session -> session.execute(TestKeyspace.CREATE));
    TestTable testTable = new TestTable();
    testTable.id = UUID.randomUUID();
    testTable.name = "测试实体";
    cassandra.execute(session -> session.execute(testTable.createCQL()));
    cassandra.mapper(TestTable.class).ifPresent(mapper -> {
      mapper.save(testTable);

      Assert.assertEquals(testTable, mapper.get(testTable.id, testTable.name));

      mapper.delete(testTable);
    });
  }

  static final class TestKeyspace {
    private static final String NAME = "cassandra";
    static final Statement CREATE =
        SchemaBuilder.createKeyspace(NAME).ifNotExists()
            .with()
            .replication(ImmutableMap.of(
                "class", "SimpleStrategy", "replication_factor", "1"));
  }

  @Table(keyspace = TestKeyspace.NAME, name = TestTable.NAME)
  static final class TestTable extends CassandraEntity {
    private static final String NAME = "cassandra";

    @PartitionKey
    public UUID id;
    @PartitionKey(1)
    public String name;

    @Override public String keyspaceName() {
      return TestKeyspace.NAME;
    }

    @Override public String tableName() {
      return NAME;
    }

    @Override public Create createCQL() {
      return super.createCQL()
          .addPartitionKey("id", DataType.uuid())
          .addClusteringColumn("name", DataType.text());
    }

    @Override public int hashCode() {
      return Objects.hash(super.hashCode(), name);
    }

    @Override public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }

      if (!(obj instanceof TestTable)) {
        return false;
      }

      TestTable other = (TestTable) obj;
      return super.equals(other)
          && Objects.equals(name, other.name);
    }

    @Override public String toString() {
      return toStringHelper()
          .add("name", name)
          .toString();
    }
  }
}