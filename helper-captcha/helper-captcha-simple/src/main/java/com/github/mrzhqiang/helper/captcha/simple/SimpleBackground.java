package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.Background;
import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static java.awt.RenderingHints.*;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

final class SimpleBackground implements Background {

    @Override
    public BufferedImage add(BufferedImage background) {
        Preconditions.checkNotNull(background, "background == null");

        int width = background.getWidth();
        int height = background.getHeight();
        // create an opaque image
        BufferedImage imageWithBackground = new BufferedImage(width, height, TYPE_INT_RGB);

        Graphics2D graph = (Graphics2D) imageWithBackground.getGraphics();
        RenderingHints hints = new RenderingHints(KEY_ANTIALIASING, VALUE_ANTIALIAS_OFF);

        hints.add(new RenderingHints(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY));
        hints.add(new RenderingHints(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY));
        hints.add(new RenderingHints(KEY_RENDERING, VALUE_RENDER_QUALITY));

        graph.setRenderingHints(hints);

        Color from = SimpleConfig.Background.FROM;
        Color to = SimpleConfig.Background.TO;
        GradientPaint paint = new GradientPaint(0, 0, from, width, height, to);
        graph.setPaint(paint);
        graph.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        graph.drawImage(background, 0, 0, null);

        return imageWithBackground;
    }
}
