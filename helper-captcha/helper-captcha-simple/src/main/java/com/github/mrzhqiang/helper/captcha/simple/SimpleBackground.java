package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.Background;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static java.awt.RenderingHints.*;
import static java.awt.image.BufferedImage.*;

public final class SimpleBackground implements Background {

    private final Config config;

    public SimpleBackground(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public BufferedImage add(BufferedImage background) {
        Preconditions.checkNotNull(background, "background == null");

        String fromColor = config.getString("background.clear.from");
        Color from = Colors.of(fromColor);
        String toColor = config.getString("background.clear.to");
        Color to = Colors.of(toColor);

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

        GradientPaint paint = new GradientPaint(0, 0, from, width, height, to);
        graph.setPaint(paint);
        graph.fill(new Rectangle2D.Double(0, 0, width, height));

        // draw the transparent image over the background
        graph.drawImage(background, 0, 0, null);

        return imageWithBackground;
    }
}
