package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.ConfigFactory;

import java.awt.image.BufferedImage;

public interface Ripple {

    String CLASS = ConfigFactory.load().getString("captcha.producer.ripple");

    BufferedImage distort(BufferedImage source);
}
