package helper.database.cassandra;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.schemabuilder.Create;
import com.datastax.driver.core.schemabuilder.SchemaBuilder;
import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.annotations.Column;
import com.google.common.base.MoreObjects;
import java.time.Instant;
import java.util.Objects;

/**
 * Cassandra 实体。
 * <p>
 * CQL 数据库除了主键之外，还有分区键，为了保证 {@link Mapper} 的正常使用，实体将只使用分区键。
 *
 * @author mrzhqiang
 */
public abstract class CassandraEntity {
  protected static final String COL_COMMON_CREATED = "created";
  protected static final String COL_COMMON_MODIFIED = "modified";
  protected static final String COL_COMMON_DESCRIPTION = "description";

  @Column(name = COL_COMMON_CREATED, codec = InstantCodec.class)
  public Instant created = Instant.now();
  @Column(name = COL_COMMON_MODIFIED, codec = InstantCodec.class)
  public Instant modified = Instant.now();
  @Column(name = COL_COMMON_DESCRIPTION)
  public String description;

  /**
   * 返回秘钥空间名字。
   *
   * @return 秘钥空间名字。
   */
  abstract public String keyspaceName();

  /**
   * 返回表名。
   *
   * @return 表名。
   */
  abstract public String tableName();

  /**
   * 返回创建语句，用来检查表是否已创建。
   *
   * @return CQL 语句类。
   */
  public Create createCQL() {
    return SchemaBuilder.createTable(keyspaceName(), tableName())
        .ifNotExists()
        .addColumn(COL_COMMON_CREATED, DataType.timestamp())
        .addColumn(COL_COMMON_MODIFIED, DataType.timestamp())
        .addColumn(COL_COMMON_DESCRIPTION, DataType.text());
  }

  /**
   * toString 的辅助方法。
   *
   * @return Guava 的 toString 辅助类。
   */
  protected MoreObjects.ToStringHelper toStringHelper() {
    return MoreObjects.toStringHelper(this)
        .add("created", created)
        .add("modified", modified)
        .add("description", description);
  }

  @Override public int hashCode() {
    return Objects.hash(created, modified, description);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof CassandraEntity)) {
      return false;
    }

    CassandraEntity other = ((CassandraEntity) obj);
    return Objects.equals(this.created, other.created)
        && Objects.equals(this.modified, other.modified)
        && Objects.equals(this.description, other.description);
  }
}
