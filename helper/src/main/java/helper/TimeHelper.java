package helper;

import com.google.common.base.Preconditions;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
   * 计算某个瞬间到现在，已过去多久。
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
   * 注意：如果这个瞬间在未来，则显示“未知”。
   *
   * @param value 某个瞬间。
   * @return 对时间间隔的文字描述，比如："1 秒钟前"、"1 小时前"。
   */
  public static String untilNow(Instant value) {
    Preconditions.checkNotNull(value);

    Instant now = Instant.now();
    if (value.isAfter(now)) {
      return "未知";
    }
    long seconds = value.until(now, ChronoUnit.SECONDS);
    if (seconds == 0) {
      return "刚刚";
    }
    long minutes = value.until(now, ChronoUnit.MINUTES);
    if (minutes == 0) {
      return String.format(FORMAT_UNTIL_SECONDS, seconds);
    }
    long hours = value.until(now, ChronoUnit.HOURS);
    if (hours == 0) {
      return String.format(FORMAT_UNTIL_MINUTES, minutes);
    }
    long days = value.until(now, ChronoUnit.DAYS);
    if (days == 0) {
      return String.format(FORMAT_UNTIL_HOURS, hours);
    }
    long months = value.until(now, ChronoUnit.MONTHS);
    if (months == 0) {
      return String.format(FORMAT_UNTIL_DAYS, days);
    }
    long years = value.until(now, ChronoUnit.YEARS);
    if (years == 0) {
      return String.format(FORMAT_UNTIL_MONTHS, months);
    }
    return String.format(FORMAT_UNTIL_YEARS, years);
  }

  /**
   * 显示某个瞬间的特定时间格式。
   * <p>
   * 1. 如果是未来：yyyy-MM-dd HH:mm:ss
   * <p>
   * 2. 如果是现在：HH:mm:ss（今天）
   * <p>
   * 3. 如果是过去：yyyy-MM-dd
   *
   * @param value 某个瞬间。
   * @return 格式化的时间字符串。
   */
  public static String display(Instant value) {
    Preconditions.checkNotNull(value);

    Instant now = Instant.now();
    if (value.isAfter(now)) {
      // yyyy-MM-dd HH:mm:ss
      return DateHelper.formatNormal(Date.from(value));
    }
    long days = value.until(now, ChronoUnit.DAYS);
    if (days == 0) {
      // HH:mm:ss
      return DateHelper.formatLocalTime(Date.from(value));
    }
    // yyyy-MM-dd
    return DateHelper.formatLocalDate(Date.from(value));
  }
}
