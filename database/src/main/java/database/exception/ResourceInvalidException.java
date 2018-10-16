package database.exception;

/**
 * 资源不可用的异常类。
 * <p>
 * 通常来说，存储在数据库中的资源都是有效的，只不过还有一种策略，在删除数据时设定资源状态为已删除，
 * 但资源实际还存储在数据库中，这称之为软删除。
 * <p>
 * 遇到对软删除的资源进行更新时，则必须抛出此异常。
 *
 * @author mrzhqiang
 */
public final class ResourceInvalidException extends RuntimeException {
  private static final long serialVersionUID = 8754691748908311928L;

  public ResourceInvalidException(String message) {
    super(message);
  }

  public ResourceInvalidException(String message, Throwable cause) {
    super(message, cause);
  }
}
