package com.github.mrzhqiang.helper.captcha.gif;

import com.github.mrzhqiang.helper.Classes;
import com.github.mrzhqiang.helper.awt.Colors;
import com.github.mrzhqiang.helper.captcha.Captcha;
import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.captcha.WordRenderer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.text.UserNames;
import com.google.common.base.Preconditions;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

public final class GifCaptcha implements Captcha {

    private static final TextProducer DEFAULT_TEXT_PRODUCER = new GifTextProducer();
    private static final WordRenderer DEFAULT_WORD_RENDERER = new GifWordRenderer();

    public static Captcha of(OutputStream out) {
        Preconditions.checkNotNull(out, "out put stream == null");
        return new GifCaptcha(out);
    }

    private final OutputStream out;

    private GifCaptcha(OutputStream out) {
        this.out = out;
    }

    @Override
    public String text() {
        String textClass = TextProducer.CLASS;
        TextProducer textProducer = Classes.ofInstance(textClass, DEFAULT_TEXT_PRODUCER);
        return textProducer.produce();
    }

    @Override
    public BufferedImage image(String text) {
        Preconditions.checkNotNull(text, "text == null");

        String wordClass = WordRenderer.CLASS;
        WordRenderer wordRenderer = Classes.ofInstance(wordClass, DEFAULT_WORD_RENDERER);
        String imageWidth = GifConfig.WIDTH;
        int width = Numbers.ofPositiveInt(imageWidth, 100);
        String imageHeight = GifConfig.HEIGHT;
        int height = Numbers.ofPositiveInt(imageHeight, 35);

        if (wordRenderer instanceof GifWordRenderer) {
            GifWordRenderer renderer = (GifWordRenderer) wordRenderer;

            int length = text.length();
            Color[] fontColor = new Color[length];
            for (int i = 0; i < length; i++) {
                fontColor[i] = Colors.of(UserNames.randomColor());
            }
            renderer.setFontColor(fontColor);

            GifEncoder gifEncoder = new GifEncoder();
            gifEncoder.setQuality(180);
            gifEncoder.setDelay(100);
            gifEncoder.setRepeat(0);
            gifEncoder.start(out);
            for (int i = 0; i < length; i++) {
                renderer.setFlag(i);
                BufferedImage frame = renderer.render(text, width, height);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
            try {
                out.close();
            } catch (IOException ignored) {
            }
            return null;
        }

        return wordRenderer.render(text, width, height);
    }
}
