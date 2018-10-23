package helper.database;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * 实体仓库。
 * <p>
 * 定义通用的 CURD 操作。
 *
 * @param <E> 仓库的实体类型。
 * @author qiang.zhang
 */
public interface Repository<E> {

  /**
   * 保存实体。
   * <p>
   * 一般来说，数据库映射器或类似的机制会自动判断保存的实体是否有主键，如果没有则将实体插入数据库；
   * 否则就更新对应实体的信息。
   * <p>
   * 当然，并非所有的数据库映射器都是这样，所以需要扩展类额外做一些事情。
   *
   * @param entity 实体。
   */
  void save(E entity);

  /**
   * 通过主键删除实体数据。
   *
   * @param primaryKeys 主键对象集合。这是因为在 CQL 数据库中，一般是 主键 + 分区键 的形式。
   */
  void delete(Object... primaryKeys);

  /**
   * 通过主键获取实体数据。
   *
   * @param primaryKeys 主键对象集合。
   * @return 可选的实体。如果存在，找到；不存在，则没有找到。
   */
  Optional<E> get(Object... primaryKeys);

  /**
   * 获取仓库的分页数据。
   *
   * @param index 页面序列号。
   * @param size 页面数量。
   * @param clause 查询子句。可以为 Null。
   * @return 分页接口。不为 Null。
   */
  Paging<E> page(int index, int size, @Nullable Map<String, Object> clause);

  /**
   * 获取仓库的最新数据。
   * <p>
   * 通常只查询前 10 条数据，这是为了保证服务器性能。
   *
   * @param clause where 查询子句。可以为 Null。
   * @return 数据列表。不为 Null，有可能为 Empty。
   */
  List<E> list(@Nullable Map<String, Object> clause);
}
