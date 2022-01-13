package com.github.mrzhqiang.helper.awt;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.awt.Font;
import java.util.List;

/**
 * 字体工具类。
 */
public final class Fonts {
    private Fonts() {
        // no instances
    }

    /**
     * 逗号分割器。
     * <p>
     * 忽略空字符串，自动裁减返回值的前后空格。
     */
    private static final Splitter DOT_SPLITTER = Splitter.on(',').omitEmptyStrings().trimResults();
    /**
     * 默认的字体数组。
     * <p>
     * 主要是在绘制 awt 图像时，有默认字体可以用。
     */
    private static final Font[] DEFAULT_FONTS = new Font[]{
            new Font("Arial", Font.BOLD, 40),
            new Font("Courier", Font.BOLD, 40)
    };

    /**
     * 通过字体名称和字体大小，生成字体数组。
     *
     * @param font     字体名称。
     * @param fontSize 字体大小。
     * @return 字体数组。默认情况下，将返回包含 Arial + Font.BOLD + 40 和 Courier + Font.BOLD + 40 这两个字体的数组。
     */
    public static Font[] of(String font, int fontSize) {
        return of(font, fontSize, DEFAULT_FONTS);
    }

    /**
     * 通过字体名称和字体大小，生成字体数组。
     *
     * @param font         字体名称。
     * @param fontSize     字体大小。
     * @param defaultFonts 默认的字体数组。
     * @return 字体数组。默认情况下，将返回 defaultFonts 字体数组。
     */
    public static Font[] of(String font, int fontSize, Font[] defaultFonts) {
        if (Strings.isNullOrEmpty(font)) {
            return defaultFonts;
        }

        List<String> fontNames = DOT_SPLITTER.splitToList(font);
        Font[] fonts = new Font[fontNames.size()];
        for (int i = 0; i < fontNames.size(); i++) {
            fonts[i] = new Font(fontNames.get(i), Font.BOLD, fontSize);
        }
        return fonts;
    }

}
