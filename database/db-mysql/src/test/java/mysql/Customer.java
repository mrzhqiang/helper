package mysql;

import com.google.common.base.MoreObjects;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author mrzhqiang
 */
@Entity
public class Customer {
  @Id
  long id;

  String name;

  public Customer(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .toString();
  }

  @Override public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Customer)) {
      return false;
    }

    Customer other = (Customer) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(name, other.name);
  }
}
