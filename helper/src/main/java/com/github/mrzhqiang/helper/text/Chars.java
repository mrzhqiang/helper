package com.github.mrzhqiang.helper.text;

import com.google.common.base.Strings;

public enum Chars {
    ;

    public static char[] of(String source, char[] defaultChars) {
        if (Strings.isNullOrEmpty(source)) {
            return defaultChars;
        }

        return source.toCharArray();
    }
}
