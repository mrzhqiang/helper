package database.exception;

/**
 * 数据库的异常类。
 * <p>
 * 一般是数据库执行过程中发生异常，通过此类进行传递。
 *
 * @author qiang.zhang
 */
public final class DatabaseException extends RuntimeException {
  private static final long serialVersionUID = -8442346827238434770L;

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
