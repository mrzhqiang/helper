package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.random.RandomStrings;
import com.google.common.base.Strings;

import java.security.SecureRandom;
import java.util.Random;

public final class SimpleTextProducer implements TextProducer {

    @Override
    public String produce() {
        String charLength = SimpleConfig.Text.LENGTH;
        int length = Numbers.ofPositiveInt(charLength, 5);
        String str = SimpleConfig.Text.STRING;
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
