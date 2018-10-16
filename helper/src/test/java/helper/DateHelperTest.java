package helper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static helper.DateHelper.*;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
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

  private Instant nowInstant;
  private Instant minutesInstant;
  private Instant hoursInstant;
  private Instant dayInstant;
  private Instant monthInstant;
  private Instant yearInstant;

  @Before
  public void setUp() {
    instant = Instant.parse("2009-06-23T14:05:30Z");
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
    String format = DateHelper.format(Date.from(instant));
    assertEquals("Tue, 23 Jun 2009 14:05:30 GMT", format);
  }

  @Test
  public void formatNormal() {
    // 2018-07-05 22:56:40
    String expected =
        ofPattern("yyyy-MM-dd HH:mm:ss").format(instant.atZone(ZoneId.systemDefault()));
    String actual = DateHelper.formatNormal(Date.from(instant));
    assertEquals(expected, actual);
  }

  @Test
  public void parse() {
    Date date = DateHelper.parse(RFC_1123_DATE_TIME.format(instant.atZone(ZoneId.systemDefault())));
    // Instant 是一个瞬间时刻，而 DateHelper.parse 方法只解析到秒，所以要进行比较必须截断到秒
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), date);
  }

  @Test
  public void parseNormal() {
    Date normalDate = DateHelper.parseNormal(
        ofPattern("yyyy-MM-dd HH:mm:ss").format(instant.atZone(ZoneId.systemDefault())));
    assertEquals(Date.from(instant.truncatedTo(SECONDS)), normalDate);
  }

  @Before
  public void before() {
    nowInstant = Instant.now();
    minutesInstant = nowInstant.minus(Duration.ofMinutes(1));
    hoursInstant = nowInstant.minus(Duration.ofHours(1));
    dayInstant = nowInstant.minus(Duration.ofDays(1));
    monthInstant = nowInstant.minus(Duration.ofDays(62));
    yearInstant = nowInstant.minus(Duration.ofDays(365));
  }

  @Test
  public void between() {
    assertEquals("刚刚", untilNow(Date.from(nowInstant)));
    assertEquals("1 分钟前", untilNow(Date.from(minutesInstant)));
    assertEquals("1 小时前", untilNow(Date.from(hoursInstant)));
    assertEquals("1 天前", untilNow(Date.from(dayInstant)));
    assertEquals("2 个月前", untilNow(Date.from(monthInstant)));
    assertEquals("1 年前", untilNow(Date.from(yearInstant)));
  }

  @Test
  public void showTime() {
    assertEquals(ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(nowInstant, ZoneId.systemDefault())),
        display(Date.from(nowInstant)));
    assertEquals(
        ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(minutesInstant, ZoneId.systemDefault())),
        display(Date.from(minutesInstant)));
    assertEquals(
        ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(hoursInstant, ZoneId.systemDefault())),
        display(Date.from(hoursInstant)));
    assertEquals(ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(dayInstant, ZoneId.systemDefault())),
        display(Date.from(dayInstant)));
    assertEquals(
        ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(monthInstant, ZoneId.systemDefault())),
        display(Date.from(monthInstant)));
    assertEquals(
        ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(yearInstant, ZoneId.systemDefault())),
        display(Date.from(yearInstant)));
  }
}