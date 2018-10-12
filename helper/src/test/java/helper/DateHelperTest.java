package helper;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.Before;
import org.junit.Test;

import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mrzhqiang
 */
public final class DateHelperTest {
  private Instant instant;

  @Before
  public void setUp() {
    instant = Instant.parse("2008-06-03T11:05:30Z");
  }

  @Test
  public void format() {
    try {
      String format = DateHelper.format(null);
      // 如果没有抛出空指针异常，那么下面这个检查会抛出 AssertionError 异常
      assertNotNull(format);
    } catch (NullPointerException ignore) {
    }
    // RFC_1123_DATE_TIME such as 'Tue, 3 Jun 2008 11:05:30 GMT'.
    //String instantFormat = RFC_1123_DATE_TIME.format(instant.atZone(ZoneId.of("GMT")));
    assertEquals("Tue, 03 Jun 2008 11:05:30 GMT", DateHelper.format(Date.from(instant)));
  }

  @Test
  public void formatNormal() {
    try {
      String format = DateHelper.formatNormal(null);
      assertNotNull(format);
    } catch (NullPointerException ignore) {
    }
    // 2018-07-05 22:56:40
    String instantFormat =
        ofPattern("yyyy-MM-dd HH:mm:ss").format(instant.atZone(ZoneId.systemDefault()));
    assertEquals(instantFormat, DateHelper.formatNormal(Date.from(instant)));
  }

  @Test
  public void parse() {
    try {
      Date date = DateHelper.parse(null);
      assertNotNull(date);
    } catch (NullPointerException ignore) {
    }
    Date date = DateHelper.parse(RFC_1123_DATE_TIME.format(instant.atZone(ZoneId.systemDefault())));
    assertNotNull(date);
    // Instant 是一个瞬间时刻，而 DateHelper.parse 方法只解析到秒，所以要进行比较必须截断到秒
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), date);
  }

  @Test
  public void parseNormal() {
    try {
      Date date = DateHelper.parseNormal(null);
      assertNotNull(date);
    } catch (NullPointerException ignore) {
    }
    Date normalDate = DateHelper.parseNormal(
        ofPattern("yyyy-MM-dd HH:mm:ss").format(instant.atZone(ZoneId.systemDefault())));
    assertNotNull(normalDate);
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), normalDate);
  }

  @Test
  public void multiThread() throws InterruptedException {
    ExecutorService service = Executors.newFixedThreadPool(10);
    Instant now = Instant.now();
    for (int i = 0; i < 10; i++) {
      service.execute(() -> {
        String format = DateHelper.format(Date.from(now));
        System.out.println(Thread.currentThread().getName() + ": " + DateHelper.parse(format));
      });
    }
    Thread.sleep(1000);
  }
}