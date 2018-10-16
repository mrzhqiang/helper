package database.exception;

/**
 * 资源已存在的异常类。
 * <p>
 * 一般来说，主键不允许重复，在保存时，如果已经存在，则抛出此异常。
 * <p>
 * 还有一种方案是，将插入和更新操作设为相同的方法，如果存在主键就更新，否则自动生成主键并插入。
 * <p>
 * 在 Jedis、Cassandra、Elasticsearch 中，主键需要外部传递；在 EBean 中，主键会自动生成。
 *
 * @author mrzhqiang
 */
public final class ResourceAlreadyExistException extends RuntimeException {
  private static final long serialVersionUID = -279820292804063045L;

  public ResourceAlreadyExistException(String message) {
    super(message);
  }

  public ResourceAlreadyExistException(String message, Throwable cause) {
    super(message, cause);
  }
}
