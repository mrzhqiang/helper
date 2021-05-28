package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Preconditions;
import com.typesafe.config.Config;

public final class ChineseTextProducer implements TextProducer {

    private final Config config;

    public ChineseTextProducer(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public String produce() {
        String charLength = config.getString("text.char.length");
        int length = Numbers.ofPositiveInt(charLength, 5);
        return RandomStrings.ofChinese(length);
    }
}
