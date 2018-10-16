package database.test;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrzhqiang
 */
public final class CustomerTest {

  @Test
  public void insertFindDelete() {
    Customer customer = new Customer("EBean!");

    EbeanServer server = Ebean.getDefaultServer();

    server.save(customer);

    Customer entity = server.find(Customer.class, 1);

    assertNotNull(entity);

    System.out.println("Hello "+ entity);

    server.delete(entity);
  }

}