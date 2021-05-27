package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.awt.Fonts;
import com.github.mrzhqiang.helper.captcha.WordRenderer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Random;

import static java.awt.RenderingHints.*;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class SimpleWordRenderer implements WordRenderer {

    private final Config config;

    public SimpleWordRenderer(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public BufferedImage render(String word, int width, int height) {
        Preconditions.checkNotNull(word, "word == null");

        BufferedImage image = new BufferedImage(width, height, TYPE_INT_ARGB);
        Graphics2D g2D = image.createGraphics();

        String fontColor = config.getString("text.font.color");
        Color color = Colors.of(fontColor, Color.BLACK);
        g2D.setColor(color);

        RenderingHints hints = new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        hints.add(new RenderingHints(KEY_RENDERING, VALUE_RENDER_QUALITY));
        g2D.setRenderingHints(hints);

        String fontSize = config.getString("text.font.size");
        int size = Numbers.ofPositiveInt(fontSize, 40);
        String fontNames = config.getString("text.font.names");
        Font[] fonts = Fonts.of(fontNames, size, WordRenderer.DEFAULT_FONTS);

        int startPosY = (height - size) / 5 + size;
        char[] wordChars = word.toCharArray();
        Font[] chosenFonts = new Font[wordChars.length];
        int[] charWidths = new int[wordChars.length];
        int widthNeeded = 0;
        FontRenderContext frc = g2D.getFontRenderContext();
        Random random = new SecureRandom();
        for (int i = 0; i < wordChars.length; i++) {
            chosenFonts[i] = fonts[random.nextInt(fonts.length)];
            char[] charToDraw = new char[]{wordChars[i]};
            GlyphVector gv = chosenFonts[i].createGlyphVector(frc, charToDraw);
            charWidths[i] = (int) gv.getVisualBounds().getWidth();
            if (i > 0) {
                widthNeeded = widthNeeded + 2;
            }
            widthNeeded = widthNeeded + charWidths[i];
        }

        String charSpace = config.getString("text.char.space");
        int space = Numbers.ofPositiveInt(charSpace, 2);
        int startPosX = (width - widthNeeded) / 2;
        for (int i = 0; i < wordChars.length; i++) {
            g2D.setFont(chosenFonts[i]);
            char[] charToDraw = new char[]{wordChars[i]};
            g2D.drawChars(charToDraw, 0, charToDraw.length, startPosX, startPosY);
            startPosX = startPosX + charWidths[i] + space;
        }
        return image;
    }
}
