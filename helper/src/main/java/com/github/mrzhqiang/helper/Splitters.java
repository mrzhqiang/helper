package com.github.mrzhqiang.helper;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.google.common.base.Splitter;

/**
 * 分离器工具。
 */
public final class Splitters {
    private Splitters() {
        // no instances
    }

    /**
     * 逗号分离器。
     */
    public static final Splitter COMMA = Splitter.on(CommonSymbols.HALF_COMMA).trimResults().omitEmptyStrings();
    /**
     * 分号分离器。
     */
    public static final Splitter SEMICOLON = Splitter.on(CommonSymbols.HALF_SEMICOLON).trimResults().omitEmptyStrings();
    /**
     * 冒号分离器。
     */
    public static final Splitter COLON = Splitter.on(CommonSymbols.HALF_COLON).trimResults().omitEmptyStrings();
    /**
     * 破折号分离器。
     */
    public static final Splitter DASH = Splitter.on(CommonSymbols.HALF_DASH).trimResults().omitEmptyStrings();
    /**
     * 点号分离器。
     */
    public static final Splitter DOT = Splitter.on(CommonSymbols.HALF_DOT).trimResults().omitEmptyStrings();
}
