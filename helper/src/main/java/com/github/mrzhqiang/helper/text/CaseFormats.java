package com.github.mrzhqiang.helper.text;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

/**
 * 大小写格式转换工具。
 */
public final class CaseFormats {
    private CaseFormats() {
        // no instances
    }

    /**
     * 从类文件转换为带 - 符号的名称。
     * <p>
     * com.randall.FssdApplication, [".qing", ".chun"] == com-randall-fssd-application.qing.chun
     * <p>
     * 需要将驼峰命名转为小写字母，中间用 - 符号分离，或者其他类似的操作，推荐使用 {@link CaseFormat} 工具。
     *
     * @param clazz 任意类的描述对象。
     * @param more  更多的后缀数组。
     * @return 按照类全名拼接好的字符串，同时替换小数点为破折号。
     */
    public static String ofCanonical(Class<?> clazz, String... more) {
        String first = clazz.getCanonicalName().toLowerCase().replaceAll("[.$]", "-");
        StringBuilder builder = new StringBuilder(first);
        for (String postfix : more) {
            builder.append(Strings.nullToEmpty(postfix));
        }
        return builder.toString();
    }

    /**
     * NamingsTest, ["1", "2", "3"] == namings-test123
     *
     * @param clazz 任意类的描述对象。
     * @param more  更多的后缀数组。
     * @return 按照简单类名拼接好的字符串，同时将驼峰命名改为破折号连接。
     */
    public static String ofSimple(Class<?> clazz, String... more) {
        String first = ofCamel(clazz.getSimpleName());
        StringBuilder builder = new StringBuilder(first);
        for (String postfix : more) {
            builder.append(Strings.nullToEmpty(postfix));
        }
        return builder.toString();
    }

    /**
     * 驼峰转小写字母和 - 连接。
     * <p>
     * CamelString == camel-string
     * <p>
     * 推荐使用 {@link CaseFormat} 工具。
     *
     * @param camel 驼峰字符串。
     * @return 修改驼峰字符串为破折号连接。
     */
    public static String ofCamel(String camel) {
        Preconditions.checkNotNull(camel, "camel == null");
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, camel);
    }
}
