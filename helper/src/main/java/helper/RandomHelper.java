package helper;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

/**
 * 随机助手。
 * <p>
 * 有时候，我们希望字符串也有默认值，这比 int == 0，以及 boolean == false 更值得期待。
 * <p>
 * 关于字符串范围：
 * <pre>
 *   0-9
 *   a-z
 *   A-Z
 *   -._~+/
 *   (百家姓)
 *   (常用汉字 3500 个)
 * </pre>
 * 关于长度范围：
 * 指定单个长度，表示只希望得到固定长度的随机字符串；
 * 指定最小和最大长度，表示在长度范围内获取随机字符串。
 * <p>
 * 关于获取字符串：
 * 通常用于游客账号、访问令牌、复杂验证码等场景。
 * <p>
 * 关于获取百家姓：
 * 用于游戏中投骰子创建姓名的类似场景。
 * <p>
 * 关于获取汉字（常用）：
 * 通常用于匿名用户、权限校验、汉字验证码等场景。
 * <pre>
 *   https://blog.csdn.net/u011762313/article/details/47419063
 * </pre>
 * <p>
 * 关于获取数字：
 * 通常用于初始密码、邮箱验证、简单验证码等场景。
 * 首字符永不为<code>0</code>，是为了保证可以解析为：
 * <pre>
 *   Short
 *   Integer
 *   Long
 * </pre>
 * 关于获取大小写字母：
 * 一般不常用，只适合特殊场景。
 *
 * @author mrzhqiang
 */
public final class RandomHelper {
  private RandomHelper() {
    throw new AssertionError("No instance.");
  }

  private static final Config CONFIG = ConfigFactory.load();

  // TODO path and constant string list

  private static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(SecureRandom::new);

  /**
   * 通过指定长度，生成随机字符序列。
   *
   * @param length 指定长度。
   * @return 随机字符串，包含：大小写字母，数字，特殊字符。
   */
  public static String getString(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    return getString(length, length);
  }

  /**
   * 通过指定最小长度和最大长度，生成随机长度的随机字符序列。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，包含：大小写字母，数字，特殊字符。
   */
  public static String getString(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      //builder.append(CHARS.charAt(RANDOM.get().nextInt(CHARS.length())));
    }
    return builder.toString();
  }

  public static String getChinese(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    return getChinese(length, length);
  }

  public static String getChinese(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      //builder.append(String.valueOf(NORMAL_CHINESE[RANDOM.get().nextInt(NORMAL_CHINESE.length)]));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度，生成随机数字序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：数字。
   */
  public static String getNumber(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    return getNumber(length, length);
  }

  /**
   * 通过指定长度范围，生成随机数字序列的字符串。
   *
   * @param min 最小长度，必须 > 0。
   * @param max 最大长度，必须 > 0，并且必须 >= min。
   * @return 随机字符串，仅包含：数字。
   */
  public static String getNumber(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    StringBuilder builder = new StringBuilder(length);
    // 第一个数字不为 0
    //builder.append(NUMBER.charAt(RANDOM.get().nextInt(NUMBER.length() - 1)));
    for (int i = 1; i < length; i++) {
      //builder.append(NUMBER.charAt(RANDOM.get().nextInt(NUMBER.length())));
    }
    return builder.toString();
  }

  /**
   * 通过指定长度，生成随机小写字母序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：小写字母。
   */
  public static String getLowerCase(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      //builder.append(LOWER_CASE.charAt(RANDOM.get().nextInt(LOWER_CASE.length())));
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
  public static String getLowerCase(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    return getLowerCase(length);
  }

  /**
   * 通过指定长度，生成随机大写字母序列的字符串。
   *
   * @param length 指定长度，必须 > 0。
   * @return 随机字符串，仅包含：大写字母。
   */
  public static String getUpperCase(int length) {
    Preconditions.checkArgument(length > 0,
        "length %s must be > 0.", length);
    StringBuilder builder = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      //builder.append(UPPER_CASE.charAt(RANDOM.get().nextInt(UPPER_CASE.length())));
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
  public static String getUpperCase(int min, int max) {
    Preconditions.checkArgument(min > 0,
        "min length %s must be > 0.", min);
    Preconditions.checkArgument(max > 0,
        "max length %s must be > 0.", max);
    Preconditions.checkArgument(max >= min,
        "max length %s must be >= min length %s.", max, min);

    int length = Math.max(min, RANDOM.get().nextInt(max));
    return getUpperCase(length);
  }
}
