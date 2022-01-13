package com.github.mrzhqiang.helper.text;

import com.google.common.base.Strings;

/**
 * 字符工具。
 */
public final class Chars {
    private Chars() {
        //  no instances
    }

    /**
     * 从指定字符串中，获取字符数组。
     *
     * @param source       指定字符串。
     * @param defaultChars 默认字符数组。
     * @return 指定字符串的数组，如果字符串为 Null 或空串，则返回默认字符数组。
     */
    public static char[] of(String source, char[] defaultChars) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultChars;
        }

        return source.toCharArray();
    }
}
