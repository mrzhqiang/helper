package com.github.mrzhqiang.helper;

import java.util.function.BinaryOperator;

/**
 * 简单的流工具。
 */
public final class SimpleStreams {

    private SimpleStreams() {
        // no instances.
    }

    /**
     * 合并旧元素。
     * <p>
     * 即当发现两个元素相等时，仅保留旧的那个元素。
     *
     * @param <T> 流中的元素。
     * @return 二元操作器。
     */
    public static <T> BinaryOperator<T> mergeOld() {
        return (oldVal, newVal) -> oldVal;
    }

}
