package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.typesafe.config.Config;

import java.security.SecureRandom;
import java.util.Random;

public final class SimpleTextProducer implements TextProducer {

    private final Config config;

    public SimpleTextProducer(Config config) {
        Preconditions.checkNotNull(config, "config == null");
        this.config = config;
    }

    @Override
    public String produce() {
        String charLength = config.getString("text.char.length");
        String str = config.getString("text.char.string");
        int length = Numbers.ofPositiveInt(charLength, 5);
        if (Strings.isNullOrEmpty(str)) {
            return RandomStrings.ofCustom(TextProducer.DEFAULT_TEXT, length);
        }

        Random random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(str.charAt(random.nextInt(str.length())));
        }
        return builder.toString();
    }
}
