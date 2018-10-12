package helper;

import com.google.common.base.Preconditions;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.annotation.Nullable;

/**
 * Date 助手.
 * <p>
 * 对 {@link Date 日期} 的格式化以及解析时间字符串为日期。
 * <p>
 * 如果你不喜欢 {@link Date 日期}，推荐 {@link java.time.format.DateTimeFormatter 日期时间格式化}。
 *
 * @author qiang.zhang
 */
public final class DateHelper {
  private DateHelper() {
    throw new AssertionError("No instance.");
  }

  /**
   * 2018-07-05 15:25:00
   */
  private static final ThreadLocal<DateFormat> DATE_FORMAT_NORMAL = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));

  /**
   * 2018-07-05
   */
  private static final ThreadLocal<DateFormat> DATE_FORMAT_LOCAL_DATE = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

  /**
   * 15:25:00
   */
  private static final ThreadLocal<DateFormat> DATE_FORMAT_LOCAL_TIME = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("HH:mm:ss", Locale.getDefault()));

  /**
   * Thu, 05 Jul 2018 14:50:45 GMT
   */
  public static String format(Date value) {
    Preconditions.checkNotNull(value);
    return HttpDate.format(value);
  }

  /**
   * 2018-07-05 22:56:40
   */
  public static String formatNormal(Date value) {
    Preconditions.checkNotNull(value);
    return DATE_FORMAT_NORMAL.get().format(value);
  }

  /**
   * 2018-07-05
   */
  public static String formatLocalDate(Date value) {
    Preconditions.checkNotNull(value);
    return DATE_FORMAT_LOCAL_DATE.get().format(value);
  }

  /**
   * 2018-07-05 22:56:40
   */
  public static String formatLocalTime(Date value) {
    Preconditions.checkNotNull(value);
    return DATE_FORMAT_LOCAL_TIME.get().format(value);
  }

  /**
   * Returns the date for {@code value}. Returns null if the value couldn't be parsed.
   */
  @Nullable
  public static Date parse(String value) {
    Preconditions.checkNotNull(value);
    return HttpDate.parse(value);
  }

  /**
   * yyyy-MM-dd HH:mm:ss, use Locale.getDefault() and TimeZone.getDefault().
   */
  @Nullable
  public static Date parseNormal(String value) {
    Preconditions.checkNotNull(value);
    try {
      return DATE_FORMAT_NORMAL.get().parse(value);
    } catch (ParseException ignore) {
      return null;
    }
  }

  /**
   * yyyy-MM-dd, use Locale.getDefault() and TimeZone.getDefault().
   */
  @Nullable
  public static Date parseLocalDate(String value) {
    Preconditions.checkNotNull(value);
    try {
      return DATE_FORMAT_LOCAL_DATE.get().parse(value);
    } catch (ParseException ignore) {
      return null;
    }
  }

  /**
   * HH:mm:ss, use Locale.getDefault() and TimeZone.getDefault().
   */
  @Nullable
  public static Date parseLocalTime(String value) {
    Preconditions.checkNotNull(value);
    try {
      return DATE_FORMAT_LOCAL_TIME.get().parse(value);
    } catch (ParseException ignore) {
      return null;
    }
  }
}
