package database;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * 仓库。
 * <p>
 * 定义通用的 CURD 操作。
 *
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
   * 删除指定主键表示的数据。
   *
   * @param primaryKeys 主键对象集合。
   */
  void delete(Object... primaryKeys);

  /**
   * 通过主键获取实体数据。
   *
   * @param primaryKeys 主键对象集合。
   * @return is Present，成功找到；is Empty，没有找到。
   */
  Optional<E> get(Object... primaryKeys);

  /**
   * 获取此仓库的实体数据列表，从指定页面开始，通过查询子句获取固定大小的数据。
   *
   * @param index 页面索引。
   * @param size 页面大小。
   * @param clause 查询子句。可以为 Null。
   * @return 分页接口。
   */
  Paging<E> list(int index, int size, @Nullable Map<String, Object> clause);

  /**
   * 获取此仓库的所有数据。
   * <p>
   * 注意：如果数量太多，有可能导致查询超时，慎用。
   *
   * @param clause 可以为 Null，这是 where 查询子句的条件。
   * @return 所有数据的列表。
   */
  List<E> list(@Nullable Map<String, Object> clause);
}
