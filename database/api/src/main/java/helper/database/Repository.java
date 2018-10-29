package helper.database;

import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * 实体仓库。
 * <p>
 * 定义通用的 CURD 操作。
 *
 * @param <E> 实体类型。
 * @author qiang.zhang
 */
public interface Repository<E> {

  /**
   * 保存实体。
   *
   * @param entity 实体。
   */
  void save(E entity);

  /**
   * 通过主键删除实体。
   *
   * @param primaryKeys 主键对象集合。因为在 CQL 数据库中，一般是 主键 + 分区键 的形式。
   */
  void delete(Object... primaryKeys);

  /**
   * 通过主键获取实体。
   *
   * @param primaryKeys 主键对象集合。因为在 CQL 数据库中，一般是 主键 + 分区键 的形式。
   * @return 可选的实体。如果不存在，则表示需要返回 NotFound 状态。
   */
  Optional<E> get(Object... primaryKeys);

  /**
   * 获取指定参数的分页。
   *
   * @param index 页面序号。
   * @param size 页面大小。
   * @param clause where 查询子句。允许为 Null。
   * @return 分页。
   */
  Paging<E> list(int index, int size, @Nullable Map<String, Object> clause);
}
