package com.github.mrzhqiang.helper;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

public enum Classes {
    ;

    @Nullable
    public static <T>T ofInstance(String className) {
        return ofInstance(className, null);
    }

    @SuppressWarnings("unchecked")
    public static <T>T ofInstance(String className, T defaultInstance) {
        if (Strings.isNullOrEmpty(className)) {
            return defaultInstance;
        }

        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception ignore) {
        }
        return defaultInstance;
    }
}
