package helper.data.mysql;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import helper.data.Paging;
import helper.data.Repository;
import helper.data.Util;
import io.ebean.ExpressionList;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.PagedList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

/**
 * 基于 EBean 的实体仓库。
 *
 * @author mrzhqiang
 */
public abstract class EBeanRepository<I, E extends EBeanModel> extends Finder<I, E>
    implements Repository<E> {

  public EBeanRepository(Class<E> type) {
    super(type);
  }

  public EBeanRepository(Class<E> type, String serverName) {
    super(type, serverName);
  }

  @Override public void save(E entity) {
    Util.execute(entity, Model::save);
  }

  @Override public void delete(Object... primaryKeys) {
    Preconditions.checkNotNull(primaryKeys);
    Preconditions.checkArgument(primaryKeys.length == 1,
        "primary key must be only one: %s", Arrays.toString(primaryKeys));

    //noinspection unchecked
    I primaryKey = (I) primaryKeys[0];
    Util.execute(primaryKey, this::deleteById);
  }

  @Override public Optional<E> get(Object... primaryKeys) {
    Preconditions.checkNotNull(primaryKeys);
    Preconditions.checkArgument(primaryKeys.length == 1,
        "primary key must be only one: %s", Arrays.toString(primaryKeys));

    //noinspection unchecked
    I primaryKey = (I) primaryKeys[0];
    return Optional.ofNullable(Util.transform(primaryKey, this::byId));
  }

  @Override public Paging<E> list(int index, int size, @Nullable Map<String, Object> clause) {
    return Util.create(() -> {
      ExpressionList<E> where = query().where();
      if (clause != null) {
        for (Map.Entry<String, Object> entry : clause.entrySet()) {
          String key = entry.getKey();
          if (Strings.isNullOrEmpty(key)) {
            continue;
          }
          Object value = entry.getValue();
          if (value != null) {
            where.eq(key, value);
          }
        }
      }

      int maxRows = Paging.computeMaxRows(size);
      int firstRow = Paging.computeFirstRow(index, maxRows);
      PagedList<E> pagedList = where.setFirstRow(firstRow)
          .setMaxRows(maxRows)
          .findPagedList();
      pagedList.loadCount();
      int total = pagedList.getTotalCount();
      int count = Paging.computePageCount(total, maxRows);
      List<E> resources = pagedList.getList();
      return Paging.of(total, index, count, resources);
    });
  }
}
