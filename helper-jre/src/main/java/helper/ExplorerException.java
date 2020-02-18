package helper;

/**
 * 资源管理器异常。
 *
 * @author mrzhqiang
 */
public final class ExplorerException extends RuntimeException {
  public ExplorerException(String message) {
    super(message);
  }

  public ExplorerException(String message, Throwable cause) {
    super(message, cause);
  }
}
