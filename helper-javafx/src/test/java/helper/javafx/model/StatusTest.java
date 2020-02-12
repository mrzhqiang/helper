package helper.javafx.model;

import io.reactivex.observers.DefaultObserver;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StatusTest extends ApplicationTest {

  private final Status status = new Status();
  private boolean first = true;

  @Test
  public void running() {
    status.observe().subscribe(new DefaultObserver<Boolean>() {
      @Override public void onNext(Boolean aBoolean) {
        if (first) {
          first = false;
          return;
        }
        assertTrue(aBoolean);
      }

      @Override public void onError(Throwable throwable) {

      }

      @Override public void onComplete() {

      }
    });
    status.running();
  }

  @Test
  public void finished() {
    status.observe().subscribe(new DefaultObserver<Boolean>() {
      @Override public void onNext(Boolean aBoolean) {
        if (first) {
          first = false;
          return;
        }
        assertFalse(aBoolean);
      }

      @Override public void onError(Throwable throwable) {

      }

      @Override public void onComplete() {

      }
    });
    status.finished();
  }
}