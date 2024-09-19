package com.github.mrzhqiang.helper;

import com.google.common.base.Strings;

/**
 * 字符串工具。
 * <p>
 * 模仿 Spring Framework 生成，后续不需要则删除。
 */
public final class StringUtils {

    private StringUtils() {
        // no instances.
    }

    public static boolean hasText(String str) {
        return !Strings.isNullOrEmpty(str);
    }

}
