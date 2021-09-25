package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.ConfigFactory;

import java.awt.image.BufferedImage;

public interface Background {

    String CLASS = ConfigFactory.load().getString("captcha.producer.background");

    BufferedImage add(BufferedImage background);
}
