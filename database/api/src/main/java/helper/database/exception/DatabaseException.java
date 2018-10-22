package helper.database.exception;

/**
 * 表示底层数据库操作异常。
 * <p>
 * 通常是数据库执行各种操作时抛出的异常，这里将它们融为一体。
 * <p>
 * 未来应该有：
 * 1. 资源已存在异常，表示创建时，主键重复；
 * 2. 资源不存在异常，表示查询或删除时，主键未找到；
 * 3. 执行异常，表示更新或删除时，某些逻辑不合法。
 *
 * @author qiang.zhang
 */
public class DatabaseException extends RuntimeException {
  private static final long serialVersionUID = -4942782720171195949L;

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
