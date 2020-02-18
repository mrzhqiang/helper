package helper.javafx.model;

import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import java.time.Instant;
import java.time.LocalDateTime;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.extern.slf4j.Slf4j;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;

/**
 * @see javafx.scene.control.TextArea
 */
@Slf4j
public class Console {
  private final StringProperty message = new SimpleStringProperty();

  public Observable<String> observe() {
    return JavaFxObservable.valuesOf(message)
        .filter(s -> !s.isEmpty())
        .map(this::formatTimestamp)
        .observeOn(JavaFxScheduler.platform());
  }

  public Observable<String> observe(long timestamp) {
    String time = ISO_LOCAL_DATE_TIME.format(Instant.ofEpochMilli(timestamp));
    return JavaFxObservable.valuesOf(message)
        .filter(s -> !s.isEmpty())
        .map(s -> formatTimestamp(time, s))
        .observeOn(JavaFxScheduler.platform());
  }

  public void log(String content) {
    log.info(content);
    message.setValue(content);
    message.setValue(null);
  }

  private String formatTimestamp(String value) {
    return formatTimestamp(ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()), value);
  }

  private String formatTimestamp(String time, String value) {
    return String.format("[%s] - %s%s", time, value, System.lineSeparator());
  }
}
