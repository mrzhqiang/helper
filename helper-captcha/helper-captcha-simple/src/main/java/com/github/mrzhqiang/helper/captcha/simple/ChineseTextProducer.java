package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.random.RandomStrings;

public final class ChineseTextProducer implements TextProducer {

    @Override
    public String produce() {
        return RandomStrings.ofChinese(Numbers.ofPositiveInt(SimpleConfig.Text.LENGTH, 5));
    }
}
