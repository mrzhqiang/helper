package com.github.mrzhqiang.helper.awt;

import com.google.common.base.Strings;

import java.awt.*;

public enum Fonts {
    ;

    private static final Font[] DEFAULT_FONTS = new Font[]{
            new Font("Arial", Font.BOLD, 40),
            new Font("Courier", Font.BOLD, 40)
    };

    public static Font[] of(String font, int fontSize) {
        return of(font, fontSize, DEFAULT_FONTS);
    }

    public static Font[] of(String font, int fontSize, Font[] defaultFonts) {
        if (Strings.isNullOrEmpty(font)) {
            return defaultFonts;
        }

        String[] fontNames = font.split(",");
        Font[] fonts = new Font[fontNames.length];
        for (int i = 0; i < fontNames.length; i++) {
            fonts[i] = new Font(fontNames[i], Font.BOLD, fontSize);
        }
        return fonts;
    }

}
