package helper.javafx;

import com.google.common.base.Preconditions;
import helper.Namings;
import helper.javafx.ui.Dialogs;
import java.net.URL;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javax.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum Applications {
  ;

  public static void start(Application application, Stage stage) {
    Preconditions.checkNotNull(application, "application == null");
    Preconditions.checkNotNull(stage, "stage == null");
    stage.setTitle(Namings.ofSimple(application.getClass()));
    start(fxml(application).orElseThrow(() -> new RuntimeException("Not found fxml")), stage,
        css(application).orElse(null));
  }

  public static void start(URL fxml, Stage primaryStage, @Nullable URL css) {
    Preconditions.checkNotNull(fxml, "fxml == null");
    Preconditions.checkNotNull(primaryStage, "primary stage == null");
    try {
      log.info("准备加载布局..");
      Parent root = FXMLLoader.load(fxml);
      if (css != null) {
        log.info("准备添加自定义样式..");
        root.getStylesheets().add(css.toExternalForm());
      }
      log.info("准备生成场景..");
      Scene scene = new Scene(root);
      primaryStage.setScene(scene);
      log.info("准备显示主舞台..");
      primaryStage.show();
    } catch (Exception e) {
      Alert alert = Dialogs.error(e);
      alert.setWidth(primaryStage.getWidth());
      alert.setOnCloseRequest(event -> {
        Platform.exit();
      });
      alert.show();
      log.error("启动 JavaFX 程序出错！", e);
    }
    primaryStage.setOnCloseRequest(event -> {
      Dialogs.confirm("是否关闭程序？将停止所有正在运行的任务！").ifPresent(buttonType -> Platform.exit());
      event.consume();
    });
  }

  public static Optional<URL> fxml(Application application) {
    Preconditions.checkNotNull(application, "application == null");
    Class<? extends Application> applicationClass = application.getClass();
    String name = Namings.ofSimple(applicationClass, ".fxml");
    return Optional.ofNullable(applicationClass.getResource("./" + name));
  }

  public static Optional<URL> css(Application application) {
    Preconditions.checkNotNull(application, "application == null");
    Class<? extends Application> applicationClass = application.getClass();
    String name = Namings.ofSimple(applicationClass, ".css");
    return Optional.ofNullable(applicationClass.getResource("./" + name));
  }
}
