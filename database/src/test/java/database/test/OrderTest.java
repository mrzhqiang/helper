package database.test;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import java.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrzhqiang
 */
public final class OrderTest {

  @Test
  public void insertFindDelete() {
    EbeanServer server = Ebean.getDefaultServer();

    Customer customer = new Customer("EBean test");
    server.save(customer);

    Order order = new Order(LocalDate.now(), customer);
    server.save(order);

    System.out.println(order);

    System.out.println(server.find(Customer.class, 1));

    server.delete(order);
    // 注意，必须先删除 order，才可以删除 customer
    server.delete(customer);
  }
}