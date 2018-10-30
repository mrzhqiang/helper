package mysql;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import io.ebean.Finder;
import java.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrzhqiang
 */
public class EBeanTest {

  @Test
  public void insertFindDelete() {
    Customer customer = new Customer("Hello EBean");

    EbeanServer server = Ebean.getDefaultServer();
    server.save(customer);

    Customer foundHello = server.find(Customer.class, 1);

    if (foundHello != null) {
      assertEquals(customer, foundHello);
    }

    server.delete(customer);
  }

  @Test
  public void placeAnOrder() {
    EbeanServer server = Ebean.getDefaultServer();

    Customer boss = new Customer("Boss");
    server.save(boss);

    Order order = new Order(LocalDate.now(), boss);
    server.save(order);

    Order foundBoosOrder = server.find(Order.class, 1);

    if (foundBoosOrder != null) {
      assertEquals(order, foundBoosOrder);
      assertEquals(boss, foundBoosOrder.customer);
    }

    server.delete(order);
  }

  @Test
  public void testExtend() {
    BaseDomain.TestBase testBase = new BaseDomain.TestBase();
    testBase.name = "test";
    testBase.registered = LocalDate.now();

    testBase.save();

    Finder<Long, BaseDomain.TestBase> FIND = new Finder<>(BaseDomain.TestBase.class);
    BaseDomain.TestBase actual = FIND.byId(1L);
    assertEquals(testBase, actual);

    actual.id = testBase.id;
    // 更新不了？
    actual.name = "test change";
    actual.save();

    actual = FIND.byId(1L);
    if (actual != null) {
      actual.delete();
    }

    EbeanServer server = Ebean.getDefaultServer();
    BaseDomain.TestDomain testDomain = new BaseDomain.TestDomain();
    Customer extend = new Customer("extend");
    server.save(extend);
    testDomain.customer = extend;
    testDomain.localDate = LocalDate.now();

    server.save(testDomain);

    assertEquals(testDomain, server.find(BaseDomain.TestDomain.class, 1));

    server.delete(testDomain);
  }
}