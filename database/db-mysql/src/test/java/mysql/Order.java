package mysql;

import com.google.common.base.MoreObjects;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author mrzhqiang
 */
@Entity
@Table(name = "order_form")
public class Order {
  @Id
  long id;

  @Column(nullable = false)
  LocalDate orderDate;

  @ManyToOne(optional = false)
  Customer customer;

  public Order(LocalDate orderDate, Customer customer) {
    this.orderDate = orderDate;
    this.customer = customer;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("orderDate", orderDate)
        .add("customer", customer)
        .toString();
  }

  @Override public int hashCode() {
    return Objects.hash(id, orderDate, customer);
  }

  @Override public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof Order)) {
      return false;
    }

    Order other = (Order) obj;
    return Objects.equals(id, other.id)
        && Objects.equals(orderDate, other.orderDate)
        && Objects.equals(customer, other.customer);
  }
}
