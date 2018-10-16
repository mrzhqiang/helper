package database.test;

import com.google.common.base.MoreObjects;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 订单。
 * <p>
 * 1. 测试 EBean 是否像 Gson 和 Jackson 那样需要默认构造方法；
 * 2. 测试 Column 行注解；
 * 3. 测试 ManyToOne 多对一注解；
 * 4. 测试 Ebean 11.5+ Ehancement 插件（注意，这个是 IDEA 的插件）。
 *
 * 提示：order 是 MySQL 关键字，所以使用了 Table 注解。
 *
 * @author mrzhqiang
 */
@Entity
@Table(name = "ebean_order")
public class Order {
  @Id
  public Long id;
  @Column(nullable = false)
  public LocalDate orderDate;
  @ManyToOne(optional = false)
  public Customer customer;

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
}
