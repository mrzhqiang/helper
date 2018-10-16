package database.exception;

/**
 * 资源不存在的异常类。
 * <p>
 * 它表示，需要删除一个指定主键，但没有找到，一般删除不需要返回值，因此发生错误则抛出此异常类。
 *
 * @author mrzhqiang
 */
public final class ResourceNotExistException extends RuntimeException {
  private static final long serialVersionUID = 3601512049852288799L;

  public ResourceNotExistException(String message) {
    super(message);
  }

  public ResourceNotExistException(String message, Throwable cause) {
    super(message, cause);
  }
}
