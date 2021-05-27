package com.github.mrzhqiang.helper.captcha;

import java.awt.image.BufferedImage;

public interface Noise {

    void make(BufferedImage image, float factor1, float factor2, float factor3, float factor4);
}
