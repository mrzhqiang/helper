package com.github.mrzhqiang.helper.javafx.ui;

import com.github.mrzhqiang.helper.StackTraces;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

/**
 * todo i18n
 */
@Slf4j
public enum Dialogs {
    ;

    private static final String DEFAULT_ERROR_MESSAGE = "抱歉！程序出现未知错误..";

    public static Alert info(String message) {
        return info(message, null);
    }

    public static Alert info(String message, @Nullable String content) {
        Preconditions.checkNotNull(message, "message == null");

        if (log.isDebugEnabled()) {
            log.debug("information={}, content={}", message, content);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("消息");
        alert.setHeaderText(message);
        if (Objects.nonNull(content)) {
            alert.setContentText(content);
        }

        return alert;
    }

    public static Alert warn(String message) {
        return warn(message, null);
    }

    public static Alert warn(String message, @Nullable String content) {
        Preconditions.checkNotNull(message, "message == null");

        log.warn("warning={}, content={}", message, content);

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(message);
        if (Objects.nonNull(content)) {
            alert.setContentText(content);
        }

        return alert;
    }

    public static Alert error(Throwable error) {
        return error(error.toString(), error);
    }

    public static Alert error(@Nullable String message, Throwable cause) {
        Preconditions.checkNotNull(cause, "cause == null");

        String errorMsg = Strings.isNullOrEmpty(message) ? DEFAULT_ERROR_MESSAGE : message;
        log.error("error={}, content={}", errorMsg, cause);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(errorMsg);
        alert.setContentText(StackTraces.of(cause));

        return alert;
    }

    public static Optional<ButtonType> confirm(String message) {
        return confirm(message, null);
    }

    public static Optional<ButtonType> confirm(String message, @Nullable String content) {
        Preconditions.checkNotNull(message, "message == null");

        if (log.isDebugEnabled()) {
            log.debug("confirmation={}, content={}", message, content);
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("请确认");
        alert.setHeaderText(message);
        if (Objects.nonNull(content)) {
            alert.setContentText(content);
        }

        return alert.showAndWait().filter(ButtonType.OK::equals);
    }
}
