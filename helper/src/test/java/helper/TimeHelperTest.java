package helper;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.Before;
import org.junit.Test;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author mrzhqiang
 */
public final class TimeHelperTest {
  private Instant nowInstant;
  private Instant minutesInstant;
  private Instant hoursInstant;
  private Instant dayInstant;
  private Instant monthInstant;
  private Instant yearInstant;

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
    try {
      String between = TimeHelper.untilNow(null);
      assertNotNull(between);
    } catch (NullPointerException ignore) {
    }

    assertEquals("刚刚", TimeHelper.untilNow(Date.from(nowInstant)));
    assertEquals("1 分钟前", TimeHelper.untilNow(Date.from(minutesInstant)));
    assertEquals("1 小时前", TimeHelper.untilNow(Date.from(hoursInstant)));
    assertEquals("1 天前", TimeHelper.untilNow(Date.from(dayInstant)));
    assertEquals("2 个月前", TimeHelper.untilNow(Date.from(monthInstant)));
    assertEquals("1 年前", TimeHelper.untilNow(Date.from(yearInstant)));
  }

  @Test
  public void showTime() {
    try {
      String display = TimeHelper.display(null);
      assertNotNull(display);
    } catch (NullPointerException ignore) {
    }

    assertEquals(ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(nowInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(nowInstant)));
    assertEquals(
        ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(minutesInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(minutesInstant)));
    assertEquals(
        ISO_LOCAL_TIME.format(LocalDateTime.ofInstant(hoursInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(hoursInstant)));
    assertEquals(ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(dayInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(dayInstant)));
    assertEquals(
        ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(monthInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(monthInstant)));
    assertEquals(
        ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(yearInstant, ZoneId.systemDefault())),
        TimeHelper.display(Date.from(yearInstant)));
  }
}