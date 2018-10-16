package database;

import io.ebean.Finder;
import io.ebean.Model;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static database.Databases.*;

/**
 * 基于 EBean 的实体仓库。
 *
 * @author qiang.zhang
 */
public abstract class EBeanRepository<I, E extends EBeanModel> extends Finder<I, E>
    implements Repository<E> {
  public EBeanRepository(Class<E> type) {
    super(type);
  }

  @Override public void save(E entity) {
    execute(entity, Model::save);
  }

  @Override public final void delete(Object... primaryKeys) {
    checkNotNull(primaryKeys);
    checkArgument(primaryKeys.length == 1, "primary key must be only one.");
    // SQL 主键一般只有一个，这里就认为是第一个
    //noinspection unchecked
    I primaryKey = (I) primaryKeys[0];
    execute(primaryKey, this::deleteById);
  }

  @Override public final Optional<E> get(Object... primaryKeys) {
    checkNotNull(primaryKeys);
    checkState(primaryKeys.length == 1, "primary key must be only one.");
    //noinspection unchecked
    I primaryKey = (I) primaryKeys[0];
    return find(primaryKey, this::byId);
  }

  @Override
  public Paging<E> list(int index, int size, @Nullable Map<String, Object> clause) {
    return create(() -> EBeans.paging(index, size, EBeans.ofMap(this, clause)));
  }

  @Override public List<E> list(@Nullable Map<String, Object> clause) {
    if (clause == null) {
      return create(this::all);
    }
    return create(() -> EBeans.ofMap(this, clause).findList());
  }
}
