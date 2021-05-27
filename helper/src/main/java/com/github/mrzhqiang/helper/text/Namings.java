package com.github.mrzhqiang.helper.text;

import com.google.common.base.Ascii;
import com.google.common.base.Preconditions;

import java.util.Objects;

public enum Namings {
    ;

    /**
     * com.randall.FssdApplication, {".qing",".chun"} == com-randall-fssdapplication.qing.chun
     *
     * @param clazz 任意类的描述对象。
     * @param more  更多的后缀数组。
     * @return 按照类全名拼接好的字符串，同时替换小数点为破折号。
     */
    public static String ofCanonical(Class<?> clazz, String... more) {
        String first = clazz.getCanonicalName().toLowerCase().replaceAll("[.$]", "-");
        StringBuilder builder = new StringBuilder(first);
        for (String postfix : more) {
            builder.append(Objects.requireNonNull(postfix));
        }
        return builder.toString();
    }

    /**
     * NamingsTest, {"1","2","3"} == namings-test123
     *
     * @param clazz 任意类的描述对象。
     * @param more  更多的后缀数组。
     * @return 按照简单类名拼接好的字符串，同时将驼峰命名改为破折号连接。
     */
    public static String ofSimple(Class<?> clazz, String... more) {
        String first = ofCamel(clazz.getSimpleName());
        StringBuilder builder = new StringBuilder(first);
        for (String postfix : more) {
            builder.append(Objects.requireNonNull(postfix));
        }
        return builder.toString();
    }

    /**
     * CamelString == camel-string
     *
     * @param camel 驼峰字符串。
     * @return 修改驼峰字符串为破折号连接。
     */
    public static String ofCamel(String camel) {
        Preconditions.checkNotNull(camel, "camel == null");

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < camel.length(); i++) {
            char c = camel.charAt(i);
            if (Ascii.isUpperCase(c)) {
                if (i > 0) {
                    builder.append("-");
                }
                builder.append(Ascii.toLowerCase(c));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
