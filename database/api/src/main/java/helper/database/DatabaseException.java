package helper.database;

/**
 * 数据库异常。
 * <p>
 * 通常是数据库执行底层操作时抛出的异常，由于各个数据库没有统一的处理规范，因此简单地捕捉
 * 所有异常，将它们归类为数据库错误。
 * <p>
 * 在数据库框架层，也就是此 api 的实现层，为了保证框架的高可用性，不应该随意抛出异常，除非是
 * 可预知的异常，比如空指针。
 * <p>
 * 在遇到非法参数和非法状态时，应该保证有默认值可用，比如分页功能，起始位置若是负数，则自动赋值
 * 为 0；查询大小若是小于阈值（一般是性能阈值），则自动赋值为阈值。
 *
 * @author qiang.zhang
 */
public final class DatabaseException extends RuntimeException {
  private static final long serialVersionUID = -4942782720171195949L;

  /**
   * 单消息构造器。
   * <p>
   * 通常是我们自己产生的异常，没有其他原因。
   *
   * @param message 消息字符串。
   */
  public DatabaseException(String message) {
    super(message);
  }

  /**
   * 消息 + 异常原因构造器。
   *
   * @param message 消息字符串。
   * @param cause 异常原因。
   */
  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
