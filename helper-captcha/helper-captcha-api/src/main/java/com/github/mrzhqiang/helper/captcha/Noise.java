package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.ConfigFactory;

import java.awt.image.BufferedImage;

public interface Noise {

    String CLASS = ConfigFactory.load().getString("captcha.producer.noise");

    void make(BufferedImage image, float factor1, float factor2, float factor3, float factor4);
}
