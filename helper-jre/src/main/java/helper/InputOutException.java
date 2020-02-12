package helper;

/**
 * IO 运行时异常。
 *
 * @author mrzhqiang
 */
public final class InputOutException extends RuntimeException {
  public InputOutException(String message) {
    super(message);
  }

  public InputOutException(String message, Throwable cause) {
    super(message, cause);
  }
}
