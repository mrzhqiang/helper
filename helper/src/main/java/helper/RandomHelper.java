package helper;

import com.google.common.base.Preconditions;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/**
 * Random 助手。
 * <p>
 * 可以生成某种类型的指定长度或长度范围区间的随机字符串。
 *
 * @author qiang.zhang
 */
public final class RandomHelper {
  private RandomHelper() {
    throw new AssertionError("No instance.");
  }

  private static final String SPECIAL = "-._~+/";
  private static final String NUMBER = "1234567890";
  private static final String LOWER_CASE = "qwertyuiopasdfghjklzxcvbnm";
  private static final String UPPER_CASE = LOWER_CASE.toUpperCase(Locale.ROOT);
  private static final String CHARS = SPECIAL + LOWER_CASE + UPPER_CASE + NUMBER;

  private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(SecureRandom::new);

  /**
   * 通过指定长度，生成随机字符序列。
   *
   * @param length 指定长度。
   * @return 随机字符串，包含：大小写字母，数字，特殊字符。
   */
  public static String asString(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    return asString(length, length);
  }

  /**
   * 通过指定最小长度和最大长度，生成随机长度的随机字符序列。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，包含：大小写字母，数字，特殊字符。
   */
  public static String asString(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(CHARS.charAt(RANDOM.get().nextInt(CHARS.length())));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度，生成随机数字序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：数字。
   */
  public static String asNumber(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    return asNumber(length, length);
  }

  /**
   * 通过指定长度范围，生成随机数字序列的字符串。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，仅包含：数字。
   */
  public static String asNumber(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    StringBuilder builder = new StringBuilder(length);
    // 第一个数字不为 0
    builder.append(NUMBER.charAt(RANDOM.get().nextInt(NUMBER.length() - 1)));
    for (int i = 1; i < length; i++) {
      builder.append(NUMBER.charAt(RANDOM.get().nextInt(NUMBER.length())));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度，生成随机小写字母序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：小写字母。
   */
  public static String asLowerCase(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(LOWER_CASE.charAt(RANDOM.get().nextInt(LOWER_CASE.length())));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度范围，生成随机小写字母序列的字符串。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，仅包含：小写字母。
   */
  public static String asLowerCase(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    return asLowerCase(length);
  }

  /**
   * 通过指定长度，生成随机大写字母序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：大写字母。
   */
  public static String asUpperCase(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      builder.append(UPPER_CASE.charAt(RANDOM.get().nextInt(UPPER_CASE.length())));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度范围，生成随机大写字母序列的字符串。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，仅包含：大写字母。
   */
  public static String asUpperCase(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    return asUpperCase(length);
  }
}
