package mysql;

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
}
