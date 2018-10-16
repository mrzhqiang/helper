package database;

import com.google.common.base.MoreObjects;
import io.ebean.Model;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.time.Instant;
import java.util.Objects;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * EBean 基础模型。
 *
 * @author mrzhqiang
 */
@MappedSuperclass
public abstract class EBeanModel extends Model {
  @Id
  public Long id;
  @Version
  public Long version;
  @WhenCreated
  public Instant created;
  @WhenModified
  public Instant modified;
  @SoftDelete
  public Boolean deleted;

  protected MoreObjects.ToStringHelper stringHelper() {
    return MoreObjects.toStringHelper(this)
        .add("编号", id)
        .add("版本", version)
        .add("创建时间", created)
        .add("修改时间", modified);
  }

  @Override public int hashCode() {
    return Objects.hash(id, version, created, modified);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof EBeanModel)) {
      return false;
    }

    EBeanModel other = (EBeanModel) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(version, other.version)
        && Objects.equals(created, other.created)
        && Objects.equals(modified, other.modified);
  }
}
