package com.github.mrzhqiang.helper;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.google.common.base.CharMatcher;

/**
 * 匹配器工具。
 */
public final class Matchers {
    private Matchers() {
        // no instances
    }

    /**
     * 纯数字匹配器。
     */
    public static final CharMatcher DIGIT = CharMatcher.inRange('0', '9');
    /**
     * 纯小写字母匹配器。
     */
    public static final CharMatcher LOWERCASE = CharMatcher.inRange('a', 'z');
    /**
     * 纯大写字母匹配器。
     */
    public static final CharMatcher UPPERCASE = CharMatcher.inRange('A', 'Z');
    /**
     * 纯大小写字母匹配器。
     */
    public static final CharMatcher LETTER = LOWERCASE.or(UPPERCASE);
    /**
     * 纯大小写字母 + 纯数字匹配器。
     */
    public static final CharMatcher LETTER_OR_DIGIT = LETTER.or(DIGIT);
    /**
     * 逗号匹配器。
     */
    public static final CharMatcher COMMA = CharMatcher.is(CommonSymbols.FULL_COMMA);
}
