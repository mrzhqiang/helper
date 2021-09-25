package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.Noise;

import java.awt.image.BufferedImage;

final class NoNoise implements Noise {

    @Override
    public void make(BufferedImage image, float factor1, float factor2, float factor3, float factor4) {
        // no-op
    }
}
