package com.github.mrzhqiang.helper.javafx;

import com.github.mrzhqiang.helper.text.CaseFormats;
import com.github.mrzhqiang.helper.javafx.ui.Dialogs;
import com.google.common.base.Preconditions;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public final class Applications {
    private Applications() {
        // no instances
    }

    public static void start(Application application, Stage stage) {
        Preconditions.checkNotNull(application, "application == null");
        Preconditions.checkNotNull(stage, "stage == null");

        stage.setTitle(CaseFormats.ofSimple(application.getClass()));
        start(fxml(application).orElseThrow(() -> new RuntimeException("Not found fxml")), stage,
                css(application).orElse(null));
    }

    public static void start(URL fxml, Stage primaryStage, @Nullable URL css) {
        Preconditions.checkNotNull(fxml, "fxml == null");
        Preconditions.checkNotNull(primaryStage, "primary stage == null");

        try {
            if (log.isDebugEnabled()) {
                log.debug("准备加载布局..");
            }
            Parent root = FXMLLoader.load(fxml);
            if (Objects.nonNull(css)) {
                if (log.isDebugEnabled()) {
                    log.debug("准备添加自定义样式..");
                }
                root.getStylesheets().add(css.toExternalForm());
            }
            if (log.isDebugEnabled()) {
                log.debug("准备生成场景..");
            }
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            if (log.isDebugEnabled()) {
                log.debug("准备显示主舞台..");
            }
            primaryStage.show();
        } catch (Exception e) {
            Alert alert = Dialogs.error(e);
            alert.setWidth(primaryStage.getWidth());
            alert.setOnCloseRequest(event -> Platform.exit());
            alert.show();
            log.error("启动 JavaFX 程序出错！", e);
        }
        primaryStage.setOnCloseRequest(event -> {
            Dialogs.confirm("是否关闭程序？将停止所有正在运行的任务！").ifPresent(buttonType -> Platform.exit());
            event.consume();
        });

        log.info("Start JavaFx Application Successful!");
    }

    public static Optional<URL> fxml(Application application) {
        Preconditions.checkNotNull(application, "application == null");

        Class<? extends Application> applicationClass = application.getClass();
        String name = CaseFormats.ofSimple(applicationClass, ".fxml");
        return Optional.ofNullable(applicationClass.getResource(name));
    }

    public static Optional<URL> css(Application application) {
        Preconditions.checkNotNull(application, "application == null");

        Class<? extends Application> applicationClass = application.getClass();
        String name = CaseFormats.ofSimple(applicationClass, ".css");
        return Optional.ofNullable(applicationClass.getResource(name));
    }
}
