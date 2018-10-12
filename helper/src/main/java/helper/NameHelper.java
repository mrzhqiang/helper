package helper;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import javax.annotation.RegEx;

/**
 * Name 助手。
 * <p>
 * 包含：获取名字首字母、通过名字确定专属颜色、检测名字是否合法、检测名字是否为中文以及一个通用的
 * 正则表达式检测名字的方法。
 *
 * @author mrzhqiang
 */
public final class NameHelper {
  private NameHelper() {
    throw new AssertionError("No instance.");
  }

  /**
   * 默认颜色常量。
   */
  private static final int DEFAULT_COLOR = 0xFF202020;
  /**
   * 预定义颜色常量数组。
   */
  private static final ImmutableList<Integer> COLORS =
      ImmutableList.of(0xFFe91e63, 0xFF9c27b0, 0xFF673ab7, 0xFF3f51b5, 0xFF5677fc, 0xFF03a9f4,
          0xFF00bcd4, 0xFF009688, 0xFFff5722, 0xFF795548, 0xFF607d8b);

  /**
   * 获取名字的第一个字符（仅限于字母或数字，包括汉字）。
   * <p>
   * 这个方法来自：
   * <pre>
   *   https://github.com/siacs/Conversations
   * </pre>
   *
   * @param value 字符串。
   * @return 传入字符串的首字母，如果传入一个空串，将使用默认字符："m"。
   */
  public static String firstLetter(String value) {
    Preconditions.checkNotNull(value);
    for (Character c : value.toCharArray()) {
      if (CharMatcher.javaLetterOrDigit().matches(c)) {
        return c.toString();
      }
    }
    // from mrzhqiang
    return "m";
  }

  /**
   * 获取名字哈希值对应的预定义 ARGB 颜色常量。
   * <p>
   * 这个方法来自<a "href"=https://github.com/siacs/Conversations>Conversations</a>。
   *
   * @param value 名字字符串，如果是 Null 或者空串，则返回 {@link #DEFAULT_COLOR}。
   * @return ARGB 颜色常量数值。
   */
  public static int color(@Nullable String value) {
    if (Strings.isNullOrEmpty(value)) {
      return DEFAULT_COLOR;
    }
    // 获得 name 的 hashCode，位与 0xFFFFFFFF——即取后 8 位
    // 再根据颜色数量取模，得到下标位置，返回对应的颜色值
    return COLORS.get((int) ((value.hashCode() & 0xFFFFFFFFL) % COLORS.size()));
  }

  /**
   * 检查字符串是否为名字。
   *
   * @param value 字符串。
   * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
   */
  public static boolean checkName(@Nullable String value) {
    return checkName(value, 0, 0);
  }

  /**
   * 检查字符串是否为名字。
   *
   * @param value 字符串。
   * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
   */
  public static boolean checkName(@Nullable String value, int min, int max) {
    boolean isValid = false;
    if (!Strings.isNullOrEmpty(value)) {
      if (CharMatcher.javaLetterOrDigit().matchesAllOf(value)) {
        isValid = true;
        int length = value.length();
        if (min > 0) {
          isValid = length >= min;
        }
        if (max > 0) {
          Preconditions.checkArgument(max >= min,
              "max length: %s must be >= min length: %s", max, min);
          isValid = length <= max;
        }
      }
    }
    return isValid;
  }

  /**
   * 检查字符串是否为中文。
   *
   * @param value 字符串。
   * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
   */
  public static boolean checkChinese(@Nullable String value) {
    return checkChinese(value, 0, 0);
  }

  /**
   * 检查字符串是否为中文，并且是否在指定长度范围内。
   *
   * @param value 字符串。
   * @param min 最小长度。
   * @param max 最大长度。
   * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
   */
  public static boolean checkChinese(@Nullable String value, int min, int max) {
    return checkString("[\\u4E00-\\u9FA5]+", value, min, max);
  }

  /**
   * 检查字符串是否匹配指定的正则表达式，并且字符串长度是否在指定范围内。
   *
   * @param regex 正则表达式。
   * @param value 字符串值。
   * @param min 最小长度。<= 0 表示不做长度检测。
   * @param max 最大长度。<= 0 表示不做长度检测。
   * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
   */
  public static boolean checkString(@RegEx String regex, @Nullable String value, int min, int max) {
    boolean isValid = false;
    if (!Strings.isNullOrEmpty(value)) {
      Preconditions.checkArgument(!Strings.isNullOrEmpty(regex),
          "invalid regex: %s", regex);
      if (Pattern.matches(regex, value)) {
        int length = value.length();
        if (min > 0) {
          isValid = length >= min;
        }
        if (max > 0) {
          Preconditions.checkArgument(max >= min,
              "max length: %s must be >= min length: %s", max, min);
          isValid = length <= max;
        }
      }
    }
    return isValid;
  }
}
