package com.github.mrzhqiang.helper;

import com.google.common.base.CharMatcher;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import javax.annotation.Nullable;
import javax.annotation.RegEx;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户名工具。
 * <p>
 * 有时候，检查一个账号或者昵称只能判断是否为 Null or Empty，为了改变这种情况，增加了名字助手类。
 * <p>
 * 关于首字母：
 * 可以处理大部分字符，通常用来生成头像标签。
 * <p>
 * 关于颜色：
 * 每个字符串都对应一组不同的颜色值。
 * <p>
 * 关于默认检测：
 * 判断字符串不为 Null 以及 Empty，并且属于 Java 传统意义上的字符或数字（阿拉伯数字只是其中一种）。
 * <p>
 * 关于中文检测：
 * 通过 Unicode 的正则表达式范围进行判断，这个值可以从配置文件修正。
 * <p>
 * 关于字符串检测：
 * 我们希望通过开放这个方法，有更灵活的检测机制。
 *
 * @author mrzhqiang
 */
public enum UserNames {
    ;

    private static final Config CONFIG = ConfigFactory.load().getConfig("helper.name");

    private static final String DEFAULT_FIRST = CONFIG.getString("first");

    private static final List<String> COLORS = CONFIG.getStringList("colors");
    private static final String DEFAULT_COLOR = COLORS.get(COLORS.size() - 1);

    private static final String REGEX_CHINESE = CONFIG.getString("regex.chinese");

    /**
     * 获取字符串的第一个字符（仅限于字母或数字，包括汉字）。
     * <p>
     * 这个方法来自：
     * <pre>
     *   https://github.com/siacs/Conversations
     * </pre>
     *
     * @param value 字符串。
     * @return 传入字符串的首字母，如果传入一个空串，将使用 {@link #DEFAULT_FIRST}。
     */
    public static String firstLetter(String value) {
        Preconditions.checkNotNull(value);
        for (Character c : value.toCharArray()) {
            if (CharMatcher.javaLetterOrDigit().matches(c)) {
                return c.toString();
            }
        }
        return DEFAULT_FIRST;
    }

    /**
     * 获取字符串哈希值对应的预定义 ARGB 颜色常量。
     * <p>
     * 这个方法来自：
     * <pre>
     *   https://github.com/siacs/Conversations
     * </pre>
     *
     * @param value 字符串，如果是 Null 或者空串，则返回 {@link #DEFAULT_COLOR}。
     * @return ARGB 颜色字符串。比如：0x202020，大多数 Color 类可以解析它，并且使用 Integer 进行解码。
     */
    public static String color(@Nullable String value) {
        if (Strings.isNullOrEmpty(value)) {
            return DEFAULT_COLOR;
        }
        // 获得 name 的 hashCode，位与 0xFFFFFFFF——即取后 8 位
        // 再根据颜色数量取模，得到下标位置，返回对应的颜色值
        return COLORS.get((int) ((value.hashCode() & 0xFFFFFFL) % COLORS.size()));
    }

    /**
     * 检查 Username 是否为字母或数字。
     *
     * @param value Username，如果是 Null 或者空串则返回 false。
     * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
     */
    public static boolean checkName(@Nullable String value) {
        return checkName(value, 0, 0);
    }

    /**
     * 检查 Username 是否为字母或数字，并位于指定范围内。
     *
     * @param value Username，如果是 Null 或者空串则返回 false。
     * @param min   最小长度。小于等于 0 表示不做长度检测。
     * @param max   最大长度。小于等于 0 表示不做长度检测，并且必须大于等于 min。
     * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
     */
    public static boolean checkName(@Nullable String value, int min, int max) {
        boolean isValid = false;
        if (!Strings.isNullOrEmpty(value)) {
            if (CharMatcher.javaLetterOrDigit().matchesAllOf(value)) {
                isValid = true;
                if (min > 0) {
                    isValid = value.length() >= min;
                }
                if (max > 0) {
                    Preconditions.checkArgument(max >= min,
                            "max length: %s must be >= min length: %s", max, min);
                    isValid = value.length() <= max;
                }
            }
        }
        return isValid;
    }

    /**
     * 检查 Username 是否为中文。
     *
     * @param value Username，如果是 Null 或者空串则返回 false。
     * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
     */
    public static boolean checkChinese(@Nullable String value) {
        return checkChinese(value, 0, 0);
    }

    /**
     * 检查 Username 是否为中文，并且是否在指定长度范围内。
     *
     * @param value Username，如果是 Null 或者空串则返回 false。
     * @param min   最小长度。小于等于 0 表示不做长度检测。
     * @param max   最大长度。小于等于 0 表示不做长度检测，并且必须大于等于 min。
     * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
     */
    public static boolean checkChinese(@Nullable String value, int min, int max) {
        return checkString(REGEX_CHINESE, value, min, max);
    }

    /**
     * 检查 Username 是否匹配指定的正则表达式，并且字符串长度是否在指定范围内。
     *
     * @param regex 正则表达式。
     * @param value Username，如果是 Null 或者空串则返回 false。
     * @param min   最小长度。小于等于 0 表示不做长度检测。
     * @param max   最大长度。小于等于 0 表示不做长度检测，并且必须大于等于 min。
     * @return true 符合规则；false 字符串值为 Null 或者不符合规则。
     */
    public static boolean checkString(@RegEx String regex, @Nullable String value, int min, int max) {
        boolean isValid = false;
        if (!Strings.isNullOrEmpty(value)) {
            Preconditions.checkArgument(!Strings.isNullOrEmpty(regex),
                    "invalid regex: %s", regex);
            if (Pattern.matches(regex, value)) {
                if (min > 0) {
                    isValid = value.length() >= min;
                }
                if (max > 0) {
                    Preconditions.checkArgument(max >= min,
                            "max length: %s must be >= min length: %s", max, min);
                    isValid = value.length() <= max;
                }
            }
        }
        return isValid;
    }
}
