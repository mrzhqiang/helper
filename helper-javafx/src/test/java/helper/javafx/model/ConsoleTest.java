package helper.javafx.model;

import io.reactivex.observers.DefaultObserver;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertTrue;

public class ConsoleTest extends ApplicationTest {

  private final Console console = new Console();

  @Test
  public void log() {
    console.observe().subscribe(new DefaultObserver<String>() {
      @Override public void onNext(String s) {
        assertTrue(s.contains("fssd"));
      }

      @Override public void onError(Throwable throwable) {
        throwable.printStackTrace();
      }

      @Override public void onComplete() {
        // no-op
      }
    });
    console.log("fssd");
  }
}