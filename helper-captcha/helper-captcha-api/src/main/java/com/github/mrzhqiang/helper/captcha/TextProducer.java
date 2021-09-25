package com.github.mrzhqiang.helper.captcha;

import com.typesafe.config.ConfigFactory;

public interface TextProducer {

    String CLASS = ConfigFactory.load().getString("captcha.producer.text");

    String DEFAULT_TEXT = "abcde2345678gfynmnpwx";

    String produce();
}
