package database.test;

import com.google.common.base.MoreObjects;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 顾客，这是一个测试 ID 是否自动增长的实体例子。
 *
 * @author mrzhqiang
 */
@Entity
public class Customer {
  @Id
  private long id;
  private String name;

  public Customer(String name) {
    this.name = name;
  }

  @Override public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .toString();
  }
}
