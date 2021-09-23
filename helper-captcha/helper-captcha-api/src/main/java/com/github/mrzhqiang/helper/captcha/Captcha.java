package com.github.mrzhqiang.helper.captcha;

import java.awt.image.BufferedImage;

public interface Captcha {

    String text();

    BufferedImage image(String text);
}
