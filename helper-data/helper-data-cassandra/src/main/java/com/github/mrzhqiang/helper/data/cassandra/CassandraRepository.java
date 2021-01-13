package com.github.mrzhqiang.helper.data.cassandra;

import com.datastax.driver.core.querybuilder.Clause;
import com.datastax.driver.core.querybuilder.Ordering;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.github.mrzhqiang.helper.data.domain.Slice;
import com.github.mrzhqiang.helper.data.repository.CrudRepository;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.*;

import static com.github.mrzhqiang.helper.data.domain.Clause.*;
import static com.github.mrzhqiang.helper.data.domain.Order.ASC;
import static com.github.mrzhqiang.helper.data.domain.Order.DESC;

/**
 * Cassandra 数据库的存储库。
 * <p>
 * Cassandra 数据库是 主键 + 分区键的形式，所以主键类型固定为 Object。
 *
 * @author mrzhqiang
 */
public interface CassandraRepository<T> extends CrudRepository<T, Object> {

    /**
     * 保存所有实体。
     *
     * @param entities 不允许是 Null 值，并且不能包含 Null 元素。
     * @return 已保存的实体集合，因为保存过程中可能会修改实体，不为 Null 值。并且数量与传入大小保持一致。
     * @throws NullPointerException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    default List<T> saveAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        List<T> results = Lists.newArrayList();
        entities.forEach(it -> Optional.ofNullable(save(it)).ifPresent(results::add));

        return results;
    }

    /**
     * 通过分页信息找到所有相关的数据，返回为切片对象。
     *
     * @param pageable 分页信息，必须不为 Null。
     * @return 切片信息，将永不为 Null。
     * @throws IllegalArgumentException 如果分页信息为 Null，则抛出非法参数异常。
     */
    default Slice<T> findAll(Pageable pageable) {
        return findAll(pageable, null);
    }

    /**
     * 通过分页信息和查询子句，找到所有相关的数据，返回为切片对象。
     *
     * @param pageable 分页信息，必须不为 Null。
     * @param where    查询子句，可以为 Null。
     * @return 切片信息，将永不为 Null。
     * @throws IllegalArgumentException 如果分页信息为 Null，则抛出非法参数异常。
     */
    Slice<T> findAll(Pageable pageable, @Nullable Map<String, Object> where);

    @Override
    default List<T> findAll() {
        return findAll(Collections.emptyMap());
    }

    /**
     * 通过查询条件检索全量数据。
     *
     * @return 包含查询到的全部数据。
     */
    List<T> findAll(@Nullable Map<String, Object> where);

    @Override
    Optional<T> findById(Object mapId);

    /**
     * 通过 ID 检索实体。
     *
     * @param ids 不允许包含 Null 值。因为在 CQL 数据库中，一般是 主键 + 分区键 的形式。
     * @return 具有 id 属性的实体，或者是一个 Optional.empty 值。
     * @throws NullPointerException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    Optional<T> findById(Object... ids);

    @Override
    List<T> findAllById(Iterable<Object> mapIds);

    @Override
    boolean existsById(Object mapId);

    /**
     * 通过 ID 组合判断是否存在该数据。
     * <p>
     * CQL 数据库通常是 主键 + 分区键 的形式。
     *
     * @throws NullPointerException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    boolean existsById(Object... ids);

    @Override
    default long count() {
        return countBy(null);
    }

    /**
     * 通过查询条件统计总数量。
     */
    long countBy(@Nullable Map<String, Object> where);

    @Override
    void deleteById(Object mapId);

    /**
     * 通过主键数组删除数据。
     *
     * @param ids 主键数组。因为在 CQL 数据库中，一般是 主键 + 分区键 的形式。
     * @throws NullPointerException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    void deleteById(Object... ids);

    @Override
    default void deleteAll(Iterable<T> entities) {
        Preconditions.checkArgument(Objects.nonNull(entities), "entities == null");

        entities.forEach(this::delete);
    }

    /**
     * 转换为查询子句列表。
     *
     * @param where 包含查询子句键值的 Map 对象。
     * @return 查询子句列表。不为 Null，可能是 Empty 列表。
     */
    static List<Clause> ofClauses(@Nullable Map<String, Object> where) {
        if (Objects.nonNull(where) && !where.isEmpty()) {
            List<Clause> clauses = Lists.newArrayListWithCapacity(where.size());
            where.forEach((name, value) -> toClause(name, value).ifPresent(clauses::add));
            return clauses;
        }

        return Collections.emptyList();
    }

    /**
     * 转换为排序语句列表。
     *
     * @param orderBy 包含排序语句的 Map 对象。
     * @return 排序语句列表，不为 Null，可能是 Empty 列表。
     */
    static List<Ordering> ofOrders(@Nullable Map<String, Object> orderBy) {
        if (Objects.nonNull(orderBy) && !orderBy.isEmpty()) {
            List<Ordering> orderings = Lists.newArrayListWithCapacity(orderBy.size());
            orderBy.forEach((s, o) -> toOrder(s, o).ifPresent(orderings::add));
            return orderings;
        }

        return Collections.emptyList();
    }

    /**
     * 将键值对转换为查询子句。
     *
     * @param name  键，可能包含 name:key 的形式，则进行特殊处理。
     * @param value 值，就是查询对象。
     * @return 可选的查询子句，如果键值对无效，那么返回可选对象的 Empty 值。
     */
    static Optional<Clause> toClause(String name, Object value) {
        if (Strings.isNullOrEmpty(name)) {
            return Optional.empty();
        }

        String key = name;
        if (name.contains(":")) {
            String[] split = name.split(":", 2);
            name = split[0];
            key = split[1];
        }

        if (Objects.nonNull(value)) {
            switch (key) {
                case GT:
                    return Optional.of(QueryBuilder.gt(name, value));
                case GTE:
                    return Optional.of(QueryBuilder.gte(name, value));
                case LT:
                    return Optional.of(QueryBuilder.lt(name, value));
                case LTE:
                    return Optional.of(QueryBuilder.lte(name, value));
                case LIKE:
                    return Optional.of(QueryBuilder.like(name, value));
                case IN:
                    return Optional.of(QueryBuilder.in(name, value));
                case CONTAINS:
                    return Optional.of(QueryBuilder.contains(name, value));
                case CONTAINS_KEY:
                    return Optional.of(QueryBuilder.containsKey(name, value));
                case NE:
                    return Optional.of(QueryBuilder.ne(name, value));
                case EQ:
                default:
                    return Optional.of(QueryBuilder.eq(name, value));
            }
        }

        if (NOT_NULL.equals(key)) {
            return Optional.of(QueryBuilder.notNull(name));
        }

        return Optional.empty();
    }

    /**
     * 将键值对转换为排序语句。
     *
     * @param name  键，就是排序类型。
     * @param value 值，就是列名称。
     * @return 可选的排序语句，如果键值对无效，那么返回可选对象的 Empty 值。
     */
    static Optional<Ordering> toOrder(String name, Object value) {
        if (Strings.isNullOrEmpty(name)) {
            return Optional.empty();
        }

        if (Objects.nonNull(value)) {
            switch (name) {
                case ASC:
                    return Optional.of(QueryBuilder.asc(String.valueOf(value)));
                case DESC:
                default:
                    return Optional.of(QueryBuilder.desc(String.valueOf(value)));
            }
        }

        return Optional.empty();
    }
}
