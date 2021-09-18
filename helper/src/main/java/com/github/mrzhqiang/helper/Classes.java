package com.github.mrzhqiang.helper;

import com.google.common.base.Strings;

import javax.annotation.Nullable;

/**
 * Java 的类工具。
 */
public enum Classes {
    ; // no instance

    /**
     * 从类名称生成类实例。
     *
     * @param className 类的全限定名称，即包括完整包名的类路径。
     * @param <T>       指定类型，通过泛型消除强制转换，有可能导致转换异常，使用者需要注意类型是否一致。
     * @return 生成的类实例。注意，可能返回 Null 值。
     */
    @Nullable
    public static <T> T ofInstance(String className) {
        return ofInstance(className, null);
    }

    /**
     * 从类名称生成类实例。
     *
     * @param className       类的全限定名称，即包括完整包名的类路径。
     * @param defaultInstance 默认实例。如果类名称为 Null 或空串，以及生成实例失败，则返回此默认实例。
     * @param <T>             指定类型，通过泛型消除强制转换，有可能导致转换异常，使用者需要注意类型是否一致。
     * @return 生成的类实例。此方法是否返回 Null 值由默认值决定。
     */
    @SuppressWarnings("unchecked")
    public static <T> T ofInstance(String className, T defaultInstance) {
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
