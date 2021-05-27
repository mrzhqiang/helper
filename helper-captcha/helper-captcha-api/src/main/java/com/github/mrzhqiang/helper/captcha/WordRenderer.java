package com.github.mrzhqiang.helper.captcha;

import java.awt.*;
import java.awt.image.BufferedImage;

public interface WordRenderer {

    Font[] DEFAULT_FONTS = new Font[]{
            new Font("Arial", Font.BOLD, 40),
            new Font("Courier", Font.BOLD, 40)
    };

    BufferedImage render(String word, int width, int height);
}
