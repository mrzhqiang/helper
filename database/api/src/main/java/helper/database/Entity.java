package helper.database;

import java.time.Instant;
import java.util.Date;

/**
 * 实体。
 * <p>
 * 规范实体的基础格式。
 * <p>
 * 不作为抽象类是有原因的，至少在 EBean 这个 ORM 框架中，抽象类是不兼容的。
 *
 * @author mrzhqiang
 */
public interface Entity {

  /**
   * 获取实体主键。
   *
   * @return 主键。一般是 字符串、长整数以及 UUID 类型。
   */
  Object primaryKey();

  /**
   * 设置实体主键。
   *
   * @param primaryKey 主键。
   */
  void setPrimaryKey(Object primaryKey);

  /**
   * 获取分区主键。
   * <p>
   * 只有 CQL 数据库才会有分区主键，如果 SQL 数据库需要的话，推荐作为外键使用。
   *
   * @return 分区键数组。一般是 字符串、长整数以及 UUID 类型。
   */
  default Object[] partitionKey() {
    return new Object[0];
  }

  /**
   * 设置分区主键。
   * <p>
   * 注意：此方法默认不做任何操作，需要扩展类实现具体逻辑。
   *
   * @param objects 分区键数组。
   */
  default void setPartitionKey(Object... objects) {
    // no-op
  }

  /**
   * 获取创建时间。
   *
   * @return 创建时间。Unix 时间。
   */
  default Instant created() {
    return Instant.now();
  }

  /**
   * 设置创建时间。
   *
   * @param created 创建时间。本地时间。
   */
  default void setCreated(Date created) {
    // no-op
  }

  /**
   * 设置创建时间。
   *
   * @param created 创建时间。Unix 时间。
   */
  default void setCreated(Instant created) {
    // no-op
  }

  /**
   * 获取修改时间。
   *
   * @return 修改时间。Unix 时间。
   */
  default Instant modified() {
    return Instant.now();
  }

  /**
   * 设置修改时间。
   *
   * @param modified 修改时间。本地时间。
   */
  default void setModified(Date modified) {
    // no-op
  }

  /**
   * 设置修改时间。
   *
   * @param modified 修改时间。Unix 时间。
   */
  default void setModified(Instant modified) {
    // no-op
  }

  /**
   * 说明、备注、简介。
   *
   * @return 说明、备注、简介。
   */
  default String description() {
    return "";
  }

  /**
   * 设置说明、备注、简介。
   *
   * @param description 说明、备注、简介。
   */
  default void setDescription(String description) {
    // no-op
  }
}
