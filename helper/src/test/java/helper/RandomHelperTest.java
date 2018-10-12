package helper;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author qiang.zhang
 */
public final class RandomHelperTest {
  @Test
  public void usernameOf() {
    String s = RandomHelper.asString(32);
    assertEquals(32, s.length());
    System.out.println(s);
  }

  @Test
  public void usernameOf1() {
    String s = RandomHelper.asString(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void numberOf() {
    String s = RandomHelper.asNumber(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void numberOf1() {
    String s = RandomHelper.asNumber(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf() {
    String s = RandomHelper.asLowerCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void lowerCaseOf1() {
    String s = RandomHelper.asLowerCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void upperCaseOf() {
    String s = RandomHelper.asUpperCase(5);
    assertEquals(5, s.length());
    System.out.println(s);
  }

  @Test
  public void upperCaseOf1() {
    String s = RandomHelper.asUpperCase(5, 10);
    assertTrue(s.length() >= 5 && s.length() < 10);
    System.out.println(s);
  }

  @Test
  public void multiThread() throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(1000);
    List<String> lists = Lists.newArrayListWithCapacity(1000 * 100);
    for (int i = 0; i < 1000; i++) {
      service.execute(() -> {
        for (int j = 0; j < 100; j++) {
          String s = RandomHelper.asString(32);
          synchronized (lists) {
            if (lists.contains(s)) {
              System.out.println(s);
            } else {
              lists.add(s);
            }
          }
        }
      });
    }
    Thread.sleep(20000);
  }

  @Test
  public void multiThread1() throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(100);
    List<String> lists = Lists.newArrayListWithCapacity(100 * 10);
    for (int i = 0; i < 100; i++) {
      service.execute(() -> {
        for (int j = 0; j < 10; j++) {
          String s = RandomHelper.asNumber(6);
          synchronized (lists) {
            if (lists.contains(s)) {
              System.out.println(s);
            } else {
              lists.add(s);
            }
          }
        }
      });
    }
    Thread.sleep(2000);
  }
}