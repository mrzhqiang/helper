package com.github.mrzhqiang.helper;

import java.io.PrintWriter;
import java.io.StringWriter;

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
}
