package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.awt.image.BufferedImage;

public interface Captcha {

    Config CONFIG = ConfigFactory.load().getConfig("captcha");

    String text();

    BufferedImage image(String text);
}
