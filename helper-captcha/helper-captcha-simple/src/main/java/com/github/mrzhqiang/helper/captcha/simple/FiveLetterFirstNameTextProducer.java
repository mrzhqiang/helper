package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

import java.util.List;

/**
 * copy from com.github.penggle.kaptcha
 */
public final class FiveLetterFirstNameTextProducer implements TextProducer {

    private final Config config;

    public FiveLetterFirstNameTextProducer(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public String produce() {
        List<String> firstNames = config.getStringList("firstName");
        return RandomStrings.ofCustom(firstNames);
    }
}
