package helper.database;

import com.google.common.base.Preconditions;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据库工具。
 * <p>
 * 这是为了将底层异常一网打尽，记录到日志中并抛出同类型异常，以便应用程序全局捕捉。
 *
 * @author qiang.zhang
 */
public final class Databases {
  private Databases() {
    throw new AssertionError("No instance.");
  }

  private static final Logger LOGGER = LoggerFactory.getLogger("database");

  /**
   * 检测方法。
   *
   * @param target 目标对象。
   * @param predicate 断言对象。
   * @param <T> 目标类型。
   * @return true 检测通过；false 检测不通过。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static <T> boolean check(T target, Predicate<T> predicate) {
    Preconditions.checkNotNull(predicate);
    try {
      return predicate.test(target);
    } catch (Exception e) {
      LOGGER.error("check error, cause: {}", e.getMessage());
      throw new DatabaseException("check failed!", e);
    }
  }

  /**
   * 创建方法。
   *
   * @param supplier 供应商对象。
   * @return 目标对象，可能为 Null。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static @Nullable <T> T create(Supplier<T> supplier) {
    Preconditions.checkNotNull(supplier);
    try {
      return supplier.get();
    } catch (Exception e) {
      LOGGER.error("create error, cause: {}", e.getMessage());
      throw new DatabaseException("create failed!", e);
    }
  }

  /**
   * 执行方法。
   *
   * @param target 目标对象。
   * @param consumer 消费者对象。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static <T> void execute(T target, Consumer<T> consumer) {
    Preconditions.checkNotNull(target);
    Preconditions.checkNotNull(consumer);
    try {
      consumer.accept(target);
    } catch (Exception e) {
      LOGGER.error("execute {} error, cause: {}", target, e.getMessage());
      throw new DatabaseException("execute failed!", e);
    }
  }

  /**
   * 映射方法。
   *
   * @param input 输入对象。
   * @param function 功能对象。
   * @param <I> 输入类型。
   * @param <R> 返回类型。
   * @return 返回对象，可能为 Null。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static @Nullable <I, R> R map(I input, Function<I, R> function) {
    Preconditions.checkNotNull(input);
    Preconditions.checkNotNull(function);
    try {
      return function.apply(input);
    } catch (Exception e) {
      LOGGER.error("map {} error, cause: {}", input, e.getMessage());
      throw new DatabaseException("map failed!", e);
    }
  }
}
