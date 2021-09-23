package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.Classes;
import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.*;
import com.github.mrzhqiang.helper.math.Numbers;
import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public final class SimpleCaptcha implements Captcha {

    private static final TextProducer DEFAULT_TEXT_PRODUCER = new SimpleTextProducer();
    private static final WordRenderer DEFAULT_WORD_RENDERER = new SimpleWordRenderer();
    private static final Ripple DEFAULT_RIPPLE = new WaterRipple();
    private static final Background DEFAULT_BACKGROUND = new SimpleBackground();

    @Override
    public String text() {
        String textClass = SimpleConfig.Producer.TEXT;
        TextProducer textProducer = Classes.ofInstance(textClass, DEFAULT_TEXT_PRODUCER);
        return textProducer.produce();
    }

    @Override
    public BufferedImage image(String text) {
        Preconditions.checkNotNull(text, "text == null");

        String wordClass = SimpleConfig.Producer.WORD;
        WordRenderer wordRenderer = Classes.ofInstance(wordClass, DEFAULT_WORD_RENDERER);
        String imageWidth = SimpleConfig.Image.WIDTH;
        int width = Numbers.ofPositiveInt(imageWidth, 200);
        String imageHeight = SimpleConfig.Image.HEIGHT;
        int height = Numbers.ofPositiveInt(imageHeight, 50);

        BufferedImage bi = wordRenderer.render(text, width, height);

        String rippleClass = SimpleConfig.Producer.RIPPLE;
        Ripple ripple = Classes.ofInstance(rippleClass, DEFAULT_RIPPLE);
        bi = ripple.distort(bi);

        String backgroundClass = SimpleConfig.Producer.BACKGROUND;
        Background background = Classes.ofInstance(backgroundClass, DEFAULT_BACKGROUND);
        bi = background.add(bi);

        Graphics2D graphics = bi.createGraphics();
        boolean borderEnabled = SimpleConfig.Border.ENABLED;
        if (borderEnabled) {
            drawBox(graphics);
        }

        return bi;
    }

    private void drawBox(Graphics2D graphics) {
        String borderColor = SimpleConfig.Border.COLOR;
        Color color = Colors.of(borderColor, Color.BLACK);
        String borderThickness = SimpleConfig.Border.THICKNESS;
        int thickness = Numbers.ofPositiveInt(borderThickness, 1);

        graphics.setColor(color);

        if (thickness != 1) {
            BasicStroke stroke = new BasicStroke((float) thickness);
            graphics.setStroke(stroke);
        }

        String imageWidth = SimpleConfig.Image.WIDTH;
        int width = Numbers.ofPositiveInt(imageWidth, 200);
        String imageHeight = SimpleConfig.Image.HEIGHT;
        int height = Numbers.ofPositiveInt(imageHeight, 50);

        //noinspection SuspiciousNameCombination
        Line2D line1 = new Line2D.Double(0, 0, 0, width);
        graphics.draw(line1);
        Line2D line2 = new Line2D.Double(0, 0, width, 0);
        graphics.draw(line2);
        line2 = new Line2D.Double(0, height - 1, width, height - 1);
        graphics.draw(line2);
        line2 = new Line2D.Double(width - 1, height - 1, width - 1, 0);
        graphics.draw(line2);
    }
}
