package com.github.mrzhqiang.helper;

import com.google.common.base.Throwables;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

public enum StackTraces {
    ;

    public static String ofCurrent() {
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            printWriter.println(element);
        }
        return writer.toString();
    }

    public static String of(Throwable e) {
        if (Environments.debug()) {
            return Throwables.getStackTraceAsString(e);
        }
        return Objects.nonNull(e) ? e.getMessage() : "Unknown Error!";
    }
}
