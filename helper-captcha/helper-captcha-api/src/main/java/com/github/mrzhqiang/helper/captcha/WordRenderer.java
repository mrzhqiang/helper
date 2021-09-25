package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.ConfigFactory;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface WordRenderer {

    String CLASS = ConfigFactory.load().getString("captcha.producer.word");

    Font[] DEFAULT_FONTS = new Font[]{
            new Font("Arial", Font.BOLD, 40),
            new Font("Courier", Font.BOLD, 40)
    };

    BufferedImage render(String word, int width, int height);
}
