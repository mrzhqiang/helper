package mysql;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrzhqiang
 */
public class CustomerTest {

  @Test
  public void insertFindDelete() {
    Customer customer = new Customer();
    customer.name = "Hello EBean";

    EbeanServer server = Ebean.getDefaultServer();
    server.save(customer);

    Customer foundHello = server.find(Customer.class, 1);

    if (foundHello != null) {
      System.out.println("hello " + foundHello.name);
    }

    server.delete(customer);
  }
}