package com.github.mrzhqiang.helper;

import com.google.common.base.Throwables;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 异常工具。
 */
public final class Exceptions {
    private Exceptions() {
        // no instances.
    }

    /**
     * 未知异常消息。
     * <p>
     * 作为异常消息的后备选择，通常不会返回此内容，除非异常实例为 null 值。
     */
    public static final String UNKNOWN_MESSAGE = "Unknown";

    /**
     * 异常痕迹。
     * <p>
     * 注意：工具类只做纯粹的事情，判断是否处于调试模式，应由调用方处理。
     *
     * @param exception 异常实例。如果传入 null 值则返回 {@link #UNKNOWN_MESSAGE 未知异常消息}。
     * @return 异常痕迹堆栈信息，不会为 null 值。
     */
    public static String ofTrace(@Nullable Exception exception) {
        return Optional.ofNullable(exception)
                .map(Throwables::getStackTraceAsString)
                .orElse(UNKNOWN_MESSAGE);
    }

    /**
     * 异常消息。
     *
     * @param exception 异常实例。如果传入 null 值则返回 {@link #UNKNOWN_MESSAGE 未知异常消息}。
     * @return 异常消息字符串，不会为 null 值。
     */
    public static String ofMessage(@Nullable Exception exception) {
        return Optional.ofNullable(exception)
                .map(Exception::getMessage)
                .orElse(UNKNOWN_MESSAGE);
    }
}
