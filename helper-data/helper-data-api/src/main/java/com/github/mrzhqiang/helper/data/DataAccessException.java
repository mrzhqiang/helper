package com.github.mrzhqiang.helper.data;

/**
 * 数据访问异常。
 * <p>
 * 通常是执行底层操作时抛出的异常，由于各个数据库没有统一的处理规范，因此简单地捕捉所有异常，
 * 将它们归类为数据库错误。
 *
 * @author qiang.zhang
 */
public final class DataAccessException extends RuntimeException {

  private static final long serialVersionUID = -4942782720171195949L;

  /**
   * 单消息构造器。
   * <p>
   * 通常是我们自己产生的异常，没有其他原因。
   *
   * @param message 消息字符串。
   */
  public DataAccessException(String message) {
    super(message);
  }

  /**
   * 消息 + 异常原因构造器。
   *
   * @param message 消息字符串。
   * @param cause 异常原因。
   */
  public DataAccessException(String message, Throwable cause) {
    super(message, cause);
  }
}
