package helper.database.mysql;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.time.Instant;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * EBean 基础模型。
 * <p>
 * 注意：IDE 应该具备实体增强功能，在 IDEA 中，可以安装 EBean 11.5+ Enhancement 插件。
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
}
