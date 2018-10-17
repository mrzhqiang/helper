package helper;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static helper.DateTimeHelper.*;
import static java.time.format.DateTimeFormatter.RFC_1123_DATE_TIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mrzhqiang
 */
public final class DateTimeHelperTest {
  private Instant instant;

  private Instant nowInstant;
  private Instant secondsInstant;
  private Instant minutesInstant;
  private Instant hoursInstant;
  private Instant dayInstant;
  private Instant monthInstant;
  private Instant yearInstant;

  @Before
  public void setUp() {
    instant = Instant.parse("2009-06-23T14:05:30Z");

    nowInstant = Instant.now();
    secondsInstant = nowInstant.minus(Duration.ofSeconds(1));
    minutesInstant = nowInstant.minus(Duration.ofMinutes(1));
    hoursInstant = nowInstant.minus(Duration.ofHours(1));
    dayInstant = nowInstant.minus(Duration.ofDays(1));
    monthInstant = nowInstant.minus(Duration.ofDays(62));
    yearInstant = nowInstant.minus(Duration.ofDays(365));
  }

  @Test
  public void format() {
    try {
      String format = DateTimeHelper.format(null);
      // 如果没有抛出空指针异常，那么下面这个检查会抛出 AssertionError 异常
      assertNotNull(format);
    } catch (NullPointerException ignore) {
    }
    String expected = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .format(instant.atZone(ZoneId.systemDefault()));
    String actual = DateTimeHelper.format(Date.from(instant));
    assertEquals(expected, actual);
  }

  @Test
  public void formatDate() {
    String expected = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        .format(instant.atZone(ZoneId.systemDefault()));
    String format = DateTimeHelper.formatDate(Date.from(instant));
    assertEquals(expected, format);
  }

  @Test
  public void formatTime() {
    String expected = DateTimeFormatter.ofPattern("HH:mm:ss")
        .format(instant.atZone(ZoneId.systemDefault()));
    String format = DateTimeHelper.formatTime(Date.from(instant));
    assertEquals(expected, format);
  }

  @Test
  public void formatHTTP() {
    String format = DateTimeHelper.formatHTTP(Date.from(instant));
    assertEquals("Tue, 23 Jun 2009 14:05:30 GMT", format);
  }

  @Test
  public void parse() {
    Date normalDate = DateTimeHelper.parse(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        .format(instant.atZone(ZoneId.systemDefault())));
    // Instant 是一个瞬间时刻，而 DateTimeHelper.parse 方法只解析到秒，所以要进行比较必须截断到秒
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), normalDate);
  }

  @Test
  public void parseHTTP() {
    Date date =
        DateTimeHelper.parseHTTP(RFC_1123_DATE_TIME.format(instant.atZone(ZoneId.systemDefault())));
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), date);
  }

  @Test
  public void between() {
    assertEquals("刚刚", untilNow(Date.from(nowInstant)));
    assertEquals("1 秒钟前", untilNow(Date.from(secondsInstant)));
    assertEquals("1 分钟前", untilNow(Date.from(minutesInstant)));
    assertEquals("1 小时前", untilNow(Date.from(hoursInstant)));
    assertEquals("1 天前", untilNow(Date.from(dayInstant)));
    assertEquals("2 月前", untilNow(Date.from(monthInstant)));
    assertEquals("1 年前", untilNow(Date.from(yearInstant)));
  }

  @Test
  public void showTime() {
    assertEquals(DateTimeHelper.formatTime(Date.from(nowInstant)),
        display(Date.from(nowInstant)));
    assertEquals(DateTimeHelper.formatTime(Date.from(secondsInstant)),
        display(Date.from(secondsInstant)));
    assertEquals(DateTimeHelper.formatTime(Date.from(minutesInstant)),
        display(Date.from(minutesInstant)));
    assertEquals(DateTimeHelper.formatTime(Date.from(hoursInstant)),
        display(Date.from(hoursInstant)));
    assertEquals(DateTimeHelper.formatDate(Date.from(dayInstant)),
        display(Date.from(dayInstant)));
    assertEquals(DateTimeHelper.formatDate(Date.from(monthInstant)),
        display(Date.from(monthInstant)));
    assertEquals(DateTimeHelper.formatDate(Date.from(yearInstant)),
        display(Date.from(yearInstant)));
  }
}
