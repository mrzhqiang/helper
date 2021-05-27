package com.github.mrzhqiang.helper.awt;

import com.google.common.base.CharMatcher;
import com.google.common.base.Strings;

import java.awt.*;
import java.lang.reflect.Field;

public enum Colors {
    ;

    private static final Color DEFAULT_COLOR = Color.BLACK;
    private static final CharMatcher LETTER_MATCHER = CharMatcher.inRange('A', 'Z')
            .or(CharMatcher.inRange('a', 'z'));

    public static Color of(String color) {
        return of(color, DEFAULT_COLOR);
    }

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
        } catch (Exception e) {
            if (LETTER_MATCHER.matchesAnyOf(color)) {
                return ofConstant(color);
            }
        }
        return defaultColor;
    }

    public static Color ofConstant(String color) {
        return ofConstant(color, DEFAULT_COLOR);
    }

    public static Color ofConstant(String color, Color defaultColor) {
        if (Strings.isNullOrEmpty(color) || LETTER_MATCHER.matchesNoneOf(color)) {
            return defaultColor;
        }

        try {
            Field field = Class.forName("java.awt.Color").getField(color);
            // If the underlying field is a static field, the obj argument is ignored; it may be null.
            return (Color) field.get(null);
        } catch (Exception ignore) {
        }
        return defaultColor;
    }
}
