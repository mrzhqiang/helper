package helper.database;

import java.util.Map;

/**
 * Redis 实体。
 * <p>
 * 鉴于 Redis 存储的基本是字符串，这里需要有一个实体接口进行转换。
 *
 * @author mrzhqiang
 */
public interface Entity {
  /**
   * 获得主键。
   *
   * @return 主键。
   */
  String primaryKey();

  /**
   * 获得内容。
   *
   * @return 内容。
   */
  Map<String, String> contentValue();
}
