package com.github.mrzhqiang.helper.awt;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

import java.awt.*;
import java.lang.reflect.Field;

/**
 * 颜色工具类。
 * <p>
 * 可以通过字符串生成 java.awt.Color 类实例。
 */
public enum Colors {
    ; // no instance

    /**
     * 默认颜色常量，黑色-BLACK：r:0 g:0 b:0
     */
    private static final Color DEFAULT_COLOR = Color.BLACK;
    /**
     * 英文单词匹配，[A--Z, a--z]
     */
    private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('A', 'Z')
            .or(CharMatcher.inRange('a', 'z'));

    /**
     * 通过字符串生成 awt 颜色类。
     *
     * @param color 字符串。包含两种形态：
     *              1. 自定义：r,g,b[,a]
     *              2. 官方：0x000000, 0X000000, #FFFFFF
     *              3. Color 常量：BLACK, RED
     * @return Color 类。永远不返回 Null 值，默认情况下返回 Color.BLACK 值。
     */
    public static Color of(String color) {
        return of(color, DEFAULT_COLOR);
    }

    /**
     * 通过字符串生成颜色类，如果字符串无效、无法解析，则返回指定的默认颜色。
     *
     * @param color        字符串。包含两种形态：
     *                     1. 自定义：r,g,b[,a]
     *                     2. 官方：0x000000, 0X000000, #FFFFFF
     *                     3. Color 常量：BLACK, RED
     * @param defaultColor 默认颜色。如果无法解析指定的颜色字符串，那么就返回此默认颜色。
     * @return Color 类。默认情况下返回 defaultColor 值。
     */
    public static Color of(String color, Color defaultColor) {
        if (Strings.isNullOrEmpty(color)) {
            return defaultColor;
        }

        if (color.contains(",")) {
            try {
                String[] colors = color.split(",");
                if (colors.length >= 3) {
                    int r = Integer.parseInt(colors[0].trim());
                    int g = Integer.parseInt(colors[1].trim());
                    int b = Integer.parseInt(colors[2].trim());
                    if (colors.length == 4) {
                        int a = Integer.parseInt(colors[3].trim());
                        return new Color(r, g, b, a);
                    }
                    return new Color(r, g, b);
                }
            } catch (Exception ignore) {
            }
            return defaultColor;
        }

        try {
            // "0x", "0X", "#", or leading zero
            return Color.decode(color);
        } catch (Exception ignore) {
            if (LETTER_MATCHER.matchesAnyOf(color)) {
                return ofConstant(color);
            }
        }
        return defaultColor;
    }

    /**
     * 从颜色字符串常量生成颜色。
     * <p>
     * 字符串通常是 Color 类的常量名。
     *
     * @param color 颜色字符串。实际上是匹配类似 Color.BLACK 的常量。
     * @return Color 颜色类。默认情况下返回 Color.BLACK 值。
     */
    public static Color ofConstant(String color) {
        return ofConstant(color, DEFAULT_COLOR);
    }

    /**
     * 从颜色字符串常量生成颜色。
     * <p>
     * 字符串通常是 Color 类的常量名。
     *
     * @param color        颜色字符串。实际上是匹配类似 Color.BLACK 的常量。
     * @param defaultColor 默认颜色。如果无法解析指定的颜色字符串，那么就返回此默认颜色。
     * @return Color 颜色类。默认情况下返回 defaultColor 值。
     */
    public static Color ofConstant(String color, Color defaultColor) {
        if (Strings.isNullOrEmpty(color) || LETTER_MATCHER.matchesNoneOf(color)) {
            return defaultColor;
        }

        try {
            Field field = Class.forName("java.awt.Color").getField(color);
            // 如果底层字段是静态字段，则忽略 obj 参数；它可以为空。
            return (Color) field.get(null);
        } catch (Exception ignore) {
        }
        return defaultColor;
    }
}