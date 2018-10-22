package helper.database.internal;

import com.google.common.base.Preconditions;
import helper.database.exception.DatabaseException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Database 助手。
 * <p>
 * 1. 初始化时，获取数据库相关实例；
 * <p>
 * 2. 增删改时，执行数据库相关操作；
 * <p>
 * 3. 查数据时，用主键查询数据实体。
 *
 * @author qiang.zhang
 */
public final class Databases {
  private Databases() {
    throw new AssertionError("No instance.");
  }

  private static final Logger LOGGER = LogManager.getLogger("database");

  /**
   * 创建方法。
   *
   * @param supplier 供应商对象。
   * @return 类型对象，可能为 Null。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static @Nullable <T> T create(Supplier<T> supplier) {
    Preconditions.checkNotNull(supplier);
    try {
      return supplier.get();
    } catch (Exception e) {
      LOGGER.error("create error, cause: %s", e.getMessage());
      throw new DatabaseException("database create failed!", e);
    }
  }

  /**
   * 执行方法。
   *
   * @param entity 实体对象。
   * @param consumer 消费者对象。
   * @throws DatabaseException 统一化的数据库异常，更容易被捕捉。
   */
  public static <E> void execute(E entity, Consumer<E> consumer) {
    Preconditions.checkNotNull(entity);
    Preconditions.checkNotNull(consumer);
    try {
      consumer.accept(entity);
    } catch (Exception e) {
      LOGGER.error("execute %s error, cause: %s", entity, e.getMessage());
      throw new DatabaseException("database execute failed!", e);
    }
  }

  /**
   * 查找方法。
   *
   * @param primary 主键对象，也可以是主键数组、主键集合。
   * @param function 查询功能对象。
   * @param <I> 主键类型，或包裹主键的数组、集合类型。
   * @param <E> 实体类型。
   * @return 可选的实体对象。
   */
  public static <I, E> Optional<E> find(I primary, Function<I, E> function) {
    Preconditions.checkNotNull(primary);
    Preconditions.checkNotNull(function);
    try {
      return Optional.ofNullable(function.apply(primary));
    } catch (Exception e) {
      LOGGER.error("find %s error, cause: %s", primary, e.getMessage());
      throw new DatabaseException("database find failed!", e);
    }
  }
}
