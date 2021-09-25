package com.github.mrzhqiang.helper.captcha.gif;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.List;

final class GifConfig {
    private GifConfig() {
        // no instance
    }

    private static final Config CONFIG = ConfigFactory.load().getConfig("captcha.gif");

    public static final String WIDTH = CONFIG.getString("width");
    public static final String HEIGHT = CONFIG.getString("height");

    public static final List<String> FONTS = CONFIG.getStringList("fonts");

    public static final class Text {
        public static final String TYPE = CONFIG.getString("text.type");
        public static final String LENGTH = CONFIG.getString("text.length");
        public static final String NUMBERS = CONFIG.getString("text.numbers");
        public static final String LETTER_UPPER = CONFIG.getString("text.letter.upper");
        public static final String LETTER_LOWER = CONFIG.getString("text.letter.lower");
        public static final String LETTER = LETTER_UPPER.concat(LETTER_LOWER);
        public static final String NUMBER_AND_LETTER_UPPER = NUMBERS.concat(LETTER_UPPER);
        public static final String CHARTER = NUMBERS.concat(LETTER);
    }

}
