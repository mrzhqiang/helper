package helper.database.cassandra;

import com.datastax.driver.extras.codecs.jdk8.InstantCodec;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.annotations.Column;
import com.google.common.base.MoreObjects;
import helper.database.Entity;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

/**
 * Cassandra 实体。
 * <p>
 * CQL 数据库除了主键之外，还有分区键，为了保证 {@link Mapper} 的正常使用，实体将只使用分区键。
 *
 * @author mrzhqiang
 */
public abstract class CassandraEntity implements Entity {
  public static final String COL_COMMON_CREATED = "created";
  public static final String COL_COMMON_MODIFIED = "modified";
  public static final String COL_COMMON_DESCRIPTION = "description";

  @Column(name = COL_COMMON_CREATED, codec = InstantCodec.class)
  public Instant created;
  @Column(name = COL_COMMON_MODIFIED, codec = InstantCodec.class)
  public Instant modified;
  @Column(name = COL_COMMON_DESCRIPTION)
  public String description;

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

  @Override public Object primaryKey() {
    throw new AssertionError("No primary key");
  }

  @Override public void setPrimaryKey(Object primaryKey) {
    throw new AssertionError("No primary key");
  }

  @Override public Instant created() {
    return created;
  }

  @Override public void setCreated(Date created) {
    this.created = created.toInstant();
  }

  @Override public void setCreated(Instant created) {
    this.created = created;
  }

  @Override public Instant modified() {
    return modified;
  }

  @Override public void setModified(Date modified) {
    this.modified = modified.toInstant();
  }

  @Override public void setModified(Instant modified) {
    this.modified = modified;
  }

  @Override public String description() {
    return description;
  }

  @Override public void setDescription(String description) {
    this.description = description;
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
