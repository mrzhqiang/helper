package helper;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import javax.annotation.Nullable;

/**
 * 日期时间助手。
 * <p>
 * 这是为了兼容与 {@link Date 日期类} 相关的逻辑，比如数据库时间戳。
 * <p>
 * 关于格式化：
 * 习惯上，我们使用 <code>yyyy-MM-dd HH:mm:ss</code> 格式，通过 {@link Config 安全配置}
 * 你将获得更加灵活的格式设定，另外还可以格式化为：仅日期、仅时间、仅 HTTP，等等。
 * <p>
 * 关于解析：
 * 字符串变化多端，如果解析失败，将返回 Null 值。
 * <p>
 * 关于配置加载：
 * 请参考 https://github.com/lightbend/config 相关内容。
 * <p>
 * 关于配置设定：
 * <pre>
 *   helper {
 *     datetime {
 *       localDatetime = "yyyy-MM-dd HH:mm:ss"
 *       localDate = "yyyy-MM-dd"
 *       localTime = "HH:mm:ss"
 *       untilUnknown = "未知"
 *       untilNow = "刚刚"
 *       untilSecond = "%d 秒钟前"
 *       untilMinute = "%d 分钟前"
 *       untilHour = "%d 小时前"
 *       untilDay = "%d 天前"
 *       untilMonth = "%d 月前"
 *       untilYear = "%d 年前"
 *     }
 *   }
 * </pre>
 * <p>
 * 关于间距：
 * 利用常识公式，优先判断范围小的时间间隔，这样可以避免间距仅 3 秒钟但位于昨天与今天交替时刻的
 * 显示为“1 天前”的错误信息。
 * <p>
 * 关于显示：
 * 通常我们希望看到本地时间，而 HTTP 以及数据库传递的是 GMT 时间，也就是 0 时区的时间，这样我们需要
 * 先得到本地时区，才能够计算出希望显示的时间。
 * <p>
 * 关于中文：
 * 目前没有做国际化规范，这个日期时间助手也只是简单的工具，所以将就着用吧。
 * <p>
 * 另外：
 * 如果你已抛弃 {@link Date Date}，那么 {@link java.time.format.DateTimeFormatter DateTimeFormatter}
 * 的相关常量解析器将是你最好的助手。
 *
 * @author mrzhqiang
 */
public final class DateTimeHelper {
  private DateTimeHelper() {
    throw new AssertionError("No instance.");
  }

  private static final ThreadLocal<DateFormat> DATE_FORMAT_LOCAL_DATETIME = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));

  private static final ThreadLocal<DateFormat> DATE_FORMAT_LOCAL_DATE = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));

  private static final ThreadLocal<DateFormat> DATE_FORMAT_LOCAL_TIME = ThreadLocal.withInitial(
      () -> new SimpleDateFormat("HH:mm:ss", Locale.getDefault()));

  private static final String FORMAT_UNTIL_YEARS = "%d 年前";
  private static final String FORMAT_UNTIL_MONTHS = "%d 个月前";
  private static final String FORMAT_UNTIL_DAYS = "%d 天前";
  private static final String FORMAT_UNTIL_HOURS = "%d 小时前";
  private static final String FORMAT_UNTIL_MINUTES = "%d 分钟前";
  private static final String FORMAT_UNTIL_SECONDS = "%d 秒钟前";
  private static final String FORMAT_UNTIL_NOW = "刚刚";
  private static final String FORMAT_UNTIL_UNKNOWN = "未知";

  /**
   * 日期 + 时间，比如：2018-07-05 22:56:40
   */
  public static String format(Date value) {
    Preconditions.checkNotNull(value);
    try {
      return DATE_FORMAT_LOCAL_DATETIME.get().format(value);
    } catch (Exception e) {
      e.printStackTrace();
      return formatHTTP(value);
    }
  }

  /**
   * 仅日期，比如：2018-07-05
   */
  public static String formatDate(Date value) {
    Preconditions.checkNotNull(value);
    return DATE_FORMAT_LOCAL_DATE.get().format(value);
  }

  /**
   * 仅时间，比如：22:56:40
   */
  public static String formatTime(Date value) {
    Preconditions.checkNotNull(value);
    return DATE_FORMAT_LOCAL_TIME.get().format(value);
  }

  /**
   * HTTP 日期 + 时间，比如：Thu, 05 Jul 2018 14:50:45 GMT
   */
  public static String formatHTTP(Date value) {
    Preconditions.checkNotNull(value);
    return HttpDate.format(value);
  }

  /**
   * 默认解析：yyyy-MM-dd HH:mm:ss, Locale.getDefault() and TimeZone.getDefault().
   * <p>
   * 如果失败，尝试使用 HTTP 格式进行解析。
   */
  @Nullable
  public static Date parse(String source) {
    Preconditions.checkNotNull(source);
    try {
      return DATE_FORMAT_LOCAL_DATETIME.get().parse(source);
    } catch (ParseException e) {
      e.printStackTrace();
      return parseHTTP(source);
    }
  }

  /**
   * Returns the date for {@code value}. Returns null if the value couldn't be parsed.
   */
  @Nullable
  public static Date parseHTTP(String value) {
    Preconditions.checkNotNull(value);
    return HttpDate.parse(value);
  }

  /**
   * 计算某个日期直到现在，已过去多久。
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

    if (value.after(new Date())) {
      return FORMAT_UNTIL_UNKNOWN;
    }
    LocalDateTime dateTime = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault());
    LocalDateTime now = LocalDateTime.now();
    long seconds = dateTime.until(now, ChronoUnit.SECONDS);
    if (seconds == 0) {
      return FORMAT_UNTIL_NOW;
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
   * 2. 如果是现在（今天）：HH:mm:ss
   * <p>
   * 3. 如果是过去：yyyy-MM-dd
   *
   * @param value 某个日期。
   * @return 格式化的时间字符串。
   */
  public static String display(Date value) {
    Preconditions.checkNotNull(value);

    if (value.after(new Date())) {
      // yyyy-MM-dd HH:mm:ss
      return format(value);
    }
    long days = LocalDateTime.ofInstant(value.toInstant(), ZoneId.systemDefault())
        .until(LocalDateTime.now(), ChronoUnit.DAYS);
    if (days == 0) {
      // HH:mm:ss
      return formatTime(value);
    }
    // yyyy-MM-dd
    return formatDate(value);
  }

  /**
   * Best-effort parser for HTTP dates.
   * <p>
   * Copy from：
   * <pre>
   *   https://github.com/square/okhttp/blob/master/okhttp/src/main/java/okhttp3/internal/http/HttpDate.java
   * </pre>
   *
   * @author square/okhttp
   */
  private static final class HttpDate {
    /**
     * GMT and UTC are equivalent for our purposes.
     */
    private static final TimeZone UTC = TimeZone.getTimeZone("GMT");

    /**
     * Most websites serve cookies in the blessed format. Eagerly create the parser to ensure such
     * cookies are on the fast path.
     */
    private static final ThreadLocal<DateFormat> STANDARD_DATE_FORMAT =
        ThreadLocal.withInitial(() -> {
          // Date format specified by RFC 7231 section 7.1.1.1.
          DateFormat rfc1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US);
          rfc1123.setLenient(false);
          rfc1123.setTimeZone(UTC);
          return rfc1123;
        });

    /**
     * If we fail to parse a date in a non-standard format, try each of these formats in sequence.
     */
    private static final String[] BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS = new String[] {
        // HTTP formats required by RFC2616 but with any timezone.
        "EEE, dd MMM yyyy HH:mm:ss zzz", // RFC 822, updated by RFC 1123 with any TZ
        "EEEE, dd-MMM-yy HH:mm:ss zzz", // RFC 850, obsoleted by RFC 1036 with any TZ.
        "EEE MMM d HH:mm:ss yyyy", // ANSI C's asctime() format
        // Alternative formats.
        "EEE, dd-MMM-yyyy HH:mm:ss z",
        "EEE, dd-MMM-yyyy HH-mm-ss z",
        "EEE, dd MMM yy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH:mm:ss z",
        "EEE dd MMM yyyy HH:mm:ss z",
        "EEE dd-MMM-yyyy HH-mm-ss z",
        "EEE dd-MMM-yy HH:mm:ss z",
        "EEE dd MMM yy HH:mm:ss z",
        "EEE,dd-MMM-yy HH:mm:ss z",
        "EEE,dd-MMM-yyyy HH:mm:ss z",
        "EEE, dd-MM-yyyy HH:mm:ss z",

        /* RI bug 6641315 claims a cookie of this format was once served by www.yahoo.com */
        "EEE MMM d yyyy HH:mm:ss z",
    };

    private static final DateFormat[] BROWSER_COMPATIBLE_DATE_FORMATS =
        new DateFormat[BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length];

    /**
     * Returns the date for {@code value}. Returns null if the value couldn't be parsed.
     */
    static Date parse(String value) {
      if (value.length() == 0) {
        return null;
      }

      ParsePosition position = new ParsePosition(0);
      Date result = STANDARD_DATE_FORMAT.get().parse(value, position);
      if (position.getIndex() == value.length()) {
        // STANDARD_DATE_FORMAT must match exactly; all text must be consumed, e.g. no ignored
        // non-standard trailing "+01:00". Those cases are covered below.
        return result;
      }
      synchronized (BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS) {
        for (int i = 0, count = BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS.length; i < count; i++) {
          DateFormat format = BROWSER_COMPATIBLE_DATE_FORMATS[i];
          if (format == null) {
            format = new SimpleDateFormat(BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS[i], Locale.US);
            // Set the timezone to use when interpreting formats that don't have a timezone. GMT is
            // specified by RFC 7231.
            format.setTimeZone(UTC);
            BROWSER_COMPATIBLE_DATE_FORMATS[i] = format;
          }
          position.setIndex(0);
          result = format.parse(value, position);
          if (position.getIndex() != 0) {
            // Something was parsed. It's possible the entire string was not consumed but we ignore
            // that. If any of the BROWSER_COMPATIBLE_DATE_FORMAT_STRINGS ended in "'GMT'" we'd have
            // to also check that position.getIndex() == value.length() otherwise parsing might have
            // terminated early, ignoring things like "+01:00". Leaving this as != 0 means that any
            // trailing junk is ignored.
            return result;
          }
        }
      }
      return null;
    }

    /**
     * Returns the string for {@code value}.
     */
    static String format(Date value) {
      return STANDARD_DATE_FORMAT.get().format(value);
    }

    private HttpDate() {
    }
  }
}
