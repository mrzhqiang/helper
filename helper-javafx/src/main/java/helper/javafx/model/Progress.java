package helper.javafx.model;

import com.google.common.base.Preconditions;
import io.reactivex.Observable;
import io.reactivex.rxjavafx.observables.JavaFxObservable;
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * @see javafx.scene.control.ProgressBar
 */
public class Progress {
  private final DoubleProperty progress = new SimpleDoubleProperty(0);

  public Observable<Double> observe() {
    return JavaFxObservable.valuesOf(progress)
        .map(Number::doubleValue)
        .map(min -> Math.min(1, min))
        .map(max -> Math.max(-1, max))
        .observeOn(JavaFxScheduler.platform());
  }

  public void update(double value) {
    progress.setValue(value);
  }

  public void compute(double current, double target) {
    Preconditions.checkArgument(current >= 0, "current: %s >= 0");
    Preconditions.checkArgument(target > 0, "target: %s > 0");
    progress.setValue(current / target);
  }
}
