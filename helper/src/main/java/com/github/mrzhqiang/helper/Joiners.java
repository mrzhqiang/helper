package com.github.mrzhqiang.helper;

import com.github.mrzhqiang.helper.text.CommonSymbols;
import com.google.common.base.Joiner;

/**
 * 聚合器工具。
 */
public final class Joiners {
    private Joiners() {
        // no instances
    }

    /**
     * 破折号聚合器。
     */
    public static final Joiner DASH = Joiner.on(CommonSymbols.HALF_DASH).skipNulls();
    /**
     * 冒号聚合器。
     */
    public static final Joiner CACHE = Joiner.on(CommonSymbols.HALF_COLON).skipNulls();
}
