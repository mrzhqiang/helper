package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.random.RandomStrings;

/**
 * copy from com.github.penggle.kaptcha
 */
public final class FiveLetterFirstNameTextProducer implements TextProducer {

    @Override
    public String produce() {
        return RandomStrings.ofCustom(SimpleConfig.FIRST_NAME);
    }
}
