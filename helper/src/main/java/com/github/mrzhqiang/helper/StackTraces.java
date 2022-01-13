package com.github.mrzhqiang.helper;

import com.google.common.base.Throwables;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * 堆栈工具。
 * <p>
 * 实际上可以用 Throwables 类替代当前类，但本工具类可以结合 IDEA 的调试模式，给出详细的堆栈内容。
 * <p>
 * 当在生产环境中运行时，可以屏蔽堆栈打印，以提升性能。
 */
public final class StackTraces {
    private StackTraces() {
        // no instances
    }

    /**
     * 获得调用当前类时的堆栈内容。
     *
     * @return 堆栈内容，从调用当前类开始逐帧输出。
     */
    public static String ofCurrent() {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            printWriter.println(element);
        }
        return writer.toString();
    }

    /**
     * 通过指定异常打印堆栈内容。
     *
     * @param e 指定可投掷的异常。
     * @return 字符串信息。当处于调试模式时，打印全部堆栈内容；否则打印异常消息。
     */
    public static String of(Throwable e) {
        if (Environments.debug()) {
            return Throwables.getStackTraceAsString(e);
        }
        return e != null ? e.getMessage() : "Unknown Error!";
    }
}
