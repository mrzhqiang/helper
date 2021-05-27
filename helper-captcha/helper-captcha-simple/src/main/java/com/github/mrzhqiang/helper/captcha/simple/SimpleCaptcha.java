package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.Classes;
import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.*;
import com.github.mrzhqiang.helper.math.Numbers;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public final class SimpleCaptcha implements Captcha {

    private final Config config;
    private final TextProducer defaultTextProducer;
    private final WordRenderer defaultWordRenderer;
    private final Ripple defaultRipple;
    private final Background defaultBackground;

    public SimpleCaptcha(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config.withFallback(Captcha.CONFIG);
        this.defaultTextProducer = new SimpleTextProducer(this.config);
        this.defaultWordRenderer = new SimpleWordRenderer(this.config);
        this.defaultRipple = new WaterRipple(this.config);
        this.defaultBackground = new SimpleBackground(this.config);
    }

    @Override
    public String text() {
        String textClass = config.getString("producer.text");
        TextProducer textProducer = Classes.ofInstance(textClass, defaultTextProducer);
        return textProducer.produce();
    }

    @Override
    public BufferedImage image(String text) {
        Preconditions.checkNotNull(text, "text == null");

        String wordClass = config.getString("producer.word");
        WordRenderer wordRenderer = Classes.ofInstance(wordClass, defaultWordRenderer);
        String imageWidth = config.getString("image.width");
        int width = Numbers.ofPositiveInt(imageWidth, 200);
        String imageHeight = config.getString("image.height");
        int height = Numbers.ofPositiveInt(imageHeight, 50);

        BufferedImage bi = wordRenderer.render(text, width, height);

        String rippleClass = config.getString("producer.ripple");
        Ripple ripple = Classes.ofInstance(rippleClass, defaultRipple);
        bi = ripple.distort(bi);

        String backgroundClass = config.getString("producer.background");
        Background background = Classes.ofInstance(backgroundClass, defaultBackground);
        bi = background.add(bi);

        Graphics2D graphics = bi.createGraphics();
        boolean borderEnabled = config.getBoolean("border.enabled");
        if (borderEnabled) {
            drawBox(graphics);
        }

        return bi;
    }

    private void drawBox(Graphics2D graphics) {
        String borderColor = config.getString("border.color");
        Color color = Colors.of(borderColor, Color.BLACK);
        String borderThickness = config.getString("border.thickness");
        int thickness = Numbers.ofPositiveInt(borderThickness, 1);

        graphics.setColor(color);

        if (thickness != 1) {
            BasicStroke stroke = new BasicStroke((float) thickness);
            graphics.setStroke(stroke);
        }

        String imageWidth = config.getString("image.width");
        int width = Numbers.ofPositiveInt(imageWidth, 200);
        String imageHeight = config.getString("image.height");
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
