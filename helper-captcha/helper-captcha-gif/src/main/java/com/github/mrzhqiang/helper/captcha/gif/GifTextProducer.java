package com.github.mrzhqiang.helper.captcha.gif;

import com.github.mrzhqiang.helper.captcha.TextProducer;
import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.helper.random.RandomStrings;

final class GifTextProducer implements TextProducer {

    @Override
    public String produce() {
        int type = Numbers.ofPositiveInt(GifConfig.Text.TYPE, 1);
        String custom = GifConfig.Text.CHARTER;
        switch (type) {
            case 6:
                custom = GifConfig.Text.NUMBER_AND_LETTER_UPPER;
                break;
            case 5:
                custom = GifConfig.Text.LETTER_LOWER;
                break;
            case 4:
                custom = GifConfig.Text.LETTER_UPPER;
                break;
            case 3:
                custom = GifConfig.Text.LETTER;
                break;
            case 2:
                custom = GifConfig.Text.NUMBERS;
                break;
            case 1:
            default:
                // charter
                break;
        }
        int length = Numbers.ofPositiveInt(GifConfig.Text.LENGTH, 4);
        return RandomStrings.ofCustom(custom, length);
    }
}
