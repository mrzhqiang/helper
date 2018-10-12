package helper;

import com.google.common.base.Preconditions;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static java.time.format.DateTimeFormatter.*;

/**
 * Time 辅助工具。
 * <p>
 * 通常用于即时聊天、论坛发帖等需要显示时间的场景。
 *
 * @author mrzhqiang
 */
public final class TimeHelper {
  private TimeHelper() {
    throw new AssertionError("No instance.");
  }

  private static final String FORMAT_UNTIL_YEARS = "%d 年前";
  private static final String FORMAT_UNTIL_MONTHS = "%d 个月前";
  private static final String FORMAT_UNTIL_DAYS = "%d 天前";
  private static final String FORMAT_UNTIL_HOURS = "%d 小时前";
  private static final String FORMAT_UNTIL_MINUTES = "%d 分钟前";
  private static final String FORMAT_UNTIL_SECONDS = "%d 秒钟前";

  /**
   * 计算某个日期到现在，已过去多久。
   * <p>
   * 比如 3 分钟，则是 3 分钟前，不会显示 1 小时前。
   * <p>
   * 换算公式：
   * <pre>
   * 1 year = 12 month
   * 1 month = 365.2425 day
   * 1 day = 24 hour
   * 1 hour = 60 minute
   * 1 minute = 60 second
   * </pre>
   * <p>
   * 注意：如果这个日期在未来，则显示“未知”。
   *
   * @param value 某个日期。
   * @return 对时间间隔的文字描述，比如："1 秒钟前"、"1 小时前"。
   */
  public static String untilNow(Date value) {
    Preconditions.checkNotNull(value);

    LocalDateTime dateTime = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
    LocalDateTime now = LocalDateTime.now();
    if (dateTime.isAfter(now)) {
      return "未知";
    }
    long seconds = dateTime.until(now, ChronoUnit.SECONDS);
    if (seconds == 0) {
      return "刚刚";
    }
    long minutes = dateTime.until(now, ChronoUnit.MINUTES);
    if (minutes == 0) {
      return String.format(FORMAT_UNTIL_SECONDS, seconds);
    }
    long hours = dateTime.until(now, ChronoUnit.HOURS);
    if (hours == 0) {
      return String.format(FORMAT_UNTIL_MINUTES, minutes);
    }
    long days = dateTime.until(now, ChronoUnit.DAYS);
    if (days == 0) {
      return String.format(FORMAT_UNTIL_HOURS, hours);
    }
    long months = dateTime.until(now, ChronoUnit.MONTHS);
    if (months == 0) {
      return String.format(FORMAT_UNTIL_DAYS, days);
    }
    long years = dateTime.until(now, ChronoUnit.YEARS);
    if (years == 0) {
      return String.format(FORMAT_UNTIL_MONTHS, months);
    }
    return String.format(FORMAT_UNTIL_YEARS, years);
  }

  /**
   * 显示某个日期的特定时间格式。
   * <p>
   * 1. 如果是未来：yyyy-MM-dd HH:mm:ss
   * <p>
   * 2. 如果是现在：HH:mm:ss（今天）
   * <p>
   * 3. 如果是过去：yyyy-MM-dd
   *
   * @param value 某个日期。
   * @return 格式化的时间字符串。
   */
  public static String display(Date value) {
    Preconditions.checkNotNull(value);

    LocalDateTime dateTime = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
    LocalDateTime now = LocalDateTime.now();
    if (dateTime.isAfter(now)) {
      // yyyy-MM-dd HH:mm:ss
      return DateHelper.formatNormal(value);
    }
    long days = dateTime.until(now, ChronoUnit.DAYS);
    if (days == 0) {
      // HH:mm:ss
      return ISO_LOCAL_TIME.format(dateTime);
    }
    // yyyy-MM-dd
    return ISO_LOCAL_DATE.format(dateTime);
  }
}
