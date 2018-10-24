package helper.database;

import java.time.Instant;

/**
 * 实体。
 * <p>
 * 规范实体的基础格式。不作为抽象类是有原因的，至少在 EBean 这个 ORM 框架中，抽象类是不兼容的。
 *
 * @author mrzhqiang
 */
public interface Entity {

  /**
   * 实体主键。
   * <p>
   * 全局唯一的存在，常用于关系型数据库，也就是 SQL 数据库。
   *
   * @return 通用类型。一般是 字符串、长整型以及 UUID 类型。
   */
  Object primaryKey();

  /**
   * 分区主键。
   * <p>
   * 只有 CQL 数据库才会有分区主键，如果 SQL 数据库需要的话，可以作为外键使用。
   *
   * @return 通用类型数组。一般是 字符串、长整型以及 UUID 类型。
   */
  default Object[] partitionKey() {
    return new Object[0];
  }

  /**
   * 创建时间。
   *
   * @return 瞬间，一个时间戳，记录为 UTC 时间。
   */
  default Instant created() {
    return Instant.now();
  }

  /**
   * 修改时间。
   *
   * @return 瞬间，一个时间戳，记录为 UTC 时间。
   */
  default Instant modified() {
    return Instant.now();
  }

  /**
   * 说明、备注、简介。
   *
   * @return 字符串。
   */
  default String description() {
    return "";
  }
}
