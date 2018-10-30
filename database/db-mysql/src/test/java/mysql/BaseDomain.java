package mysql;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

/**
 * @author mrzhqiang
 */
@MappedSuperclass
public abstract class BaseDomain extends Model {
  @Id
  Long id;
  @Version
  Long version;
  @WhenCreated
  Instant whenCreated;
  @WhenModified
  Instant whenModified;

  @Entity
  public static class TestBase extends BaseDomain {
    String name;
    LocalDate registered;
  }

  @Entity
  public static class TestDomain extends BaseDomain {
    @ManyToOne
    Customer customer;
    LocalDate localDate;
  }
}
