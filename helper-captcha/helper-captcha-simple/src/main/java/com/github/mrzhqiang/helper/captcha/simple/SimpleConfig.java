package com.github.mrzhqiang.helper.captcha.simple;

import com.github.mrzhqiang.helper.awt.Colors;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.awt.*;
import java.util.List;

final class SimpleConfig {
    private SimpleConfig() {
        // no instance
    }

    private static final Config CONFIG = ConfigFactory.load().getConfig("captcha.simple");

    public static final List<String> FIRST_NAME = CONFIG.getStringList("firstName");

    public static final class Border {
        public static final boolean ENABLED = CONFIG.getBoolean("border.enabled");
        public static final String COLOR = CONFIG.getString("border.color");
        public static final String THICKNESS = CONFIG.getString("border.thickness");
    }

    public static final class Noise {
        public static final String COLOR = CONFIG.getString("noise.color");
    }

    public static final class Text {
        public static final String LENGTH = CONFIG.getString("text.char.length");
        public static final String STRING = CONFIG.getString("text.char.string");
        public static final String SPACE = CONFIG.getString("text.char.space");
        public static final String NAMES = CONFIG.getString("text.font.names");
        public static final String COLOR = CONFIG.getString("text.font.color");
        public static final String SIZE = CONFIG.getString("text.font.size");
    }

    public static final class Image {
        public static final String WIDTH = CONFIG.getString("image.width");
        public static final String HEIGHT = CONFIG.getString("image.height");
    }

    public static final class Background {
        public static final Color FROM = Colors.of(CONFIG.getString("background.clear.from"));
        public static final Color TO = Colors.of(CONFIG.getString("background.clear.to"));
    }

}
