package com.github.mrzhqiang.helper.data.ebean;

import com.github.mrzhqiang.helper.data.domain.Page;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.github.mrzhqiang.helper.data.domain.Paging;
import com.github.mrzhqiang.helper.data.repository.PagingRepository;
import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.ebean.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.mrzhqiang.helper.data.domain.Clause.*;
import static com.github.mrzhqiang.helper.data.domain.Order.ASC;
import static com.github.mrzhqiang.helper.data.domain.Order.DESC;

/**
 * EBean 存储库接口。
 * <p>
 * 提供模板实现，以简化操作。
 */
public interface EBeanRepository<T, ID> extends PagingRepository<T, ID> {

    @Override
    default boolean existsById(ID id) {
        return Datas.test(id, it -> findById(it).isPresent());
    }

    @Override
    default List<T> findAllById(Iterable<ID> ids) {
        Preconditions.checkArgument(Objects.nonNull(ids), "ids == null");

        return Lists.newArrayList(ids).stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    default void deleteInBatch(Iterable<T> entities) {
        deleteAll(entities);
    }

    default void deleteAllInBatch() {
        deleteAll();
    }

    @Nullable
    default T getOne(ID id) {
        return findById(id).orElse(null);
    }

    /**
     * 转换为查询表达式列表。
     *
     * @param where 包含查询条件的 Map 对象。
     * @return 查询表达式列表。不为 Null，可能是 Empty 实例。
     */
    static <T> ExpressionList<T> ofClauses(ExpressionList<T> expressionList, @Nullable Map<String, Object> where) {
        Preconditions.checkNotNull(expressionList, "expression list == null");

        if (Objects.nonNull(where) && !where.isEmpty()) {
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                expressionList = toClause(expressionList, entry.getKey(), entry.getValue());
            }
            return expressionList;
        }

        return expressionList;
    }

    /**
     * 将键值对转换为查询表达式列表。
     *
     * @param expressionList 查询表达式列表。
     * @param name           键，可能包含 name:key 的形式，则进行特殊处理。
     * @param value          值，就是查询的对象。
     */
    static <T> ExpressionList<T> toClause(ExpressionList<T> expressionList, String name, Object value) {
        Preconditions.checkNotNull(expressionList, "expression list == null");

        if (Strings.isNullOrEmpty(name)) {
            return expressionList;
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
                    return expressionList.gt(name, value);
                case GTE:
                    return expressionList.ge(name, value);
                case LT:
                    return expressionList.lt(name, value);
                case LTE:
                    return expressionList.le(name, value);
                case LIKE:
                    return expressionList.like(name, String.valueOf(value));
                case IN:
                    return expressionList.in(name, value);
                case CONTAINS:
                    return expressionList.contains(name, String.valueOf(value));
//                case CONTAINS_KEY:
//                    return expressionList.ca(name, value));
                case NE:
                    return expressionList.ne(name, value);
                case EQ:
                default:
                    return expressionList.eq(name, value);
            }
        }

        if (NOT_NULL.equals(key)) {
            return expressionList.isNotNull(name);
        }

        return expressionList;
    }

    /**
     * 转换为查询语句。
     *
     * @param expressionList 查询表达式列表。
     * @param where          包含查询子句键值的 Map 对象。
     * @return 查询语句。不为 Null，可能是 Empty 实例。
     */
    static <T> Query<T> ofOrders(ExpressionList<T> expressionList, @Nullable Map<String, Object> where) {
        Preconditions.checkNotNull(expressionList, "expression list == null");

        if (Objects.nonNull(where) && !where.isEmpty()) {
            for (Map.Entry<String, Object> entry : where.entrySet()) {
                return toOrder(expressionList, entry.getKey(), entry.getValue());
            }
        }

        return expressionList.query();
    }

    /**
     * 将键值对转换为排序查询语句。
     *
     * @param expressionList 查询表达式列表。
     * @param name           键，就是排序类型。
     * @param value          值，就是列名称。
     * @return 排序语句，如果键值对无效，那么返回可选对象的 Empty 实例。
     */
    static <T> Query<T> toOrder(ExpressionList<T> expressionList, String name, Object value) {
        Preconditions.checkNotNull(expressionList, "expression list == null");

        OrderBy<T> orderBy = expressionList.orderBy();

        if (Strings.isNullOrEmpty(name)) {
            return orderBy.getQuery();
        }

        if (Objects.nonNull(value)) {
            switch (name) {
                case ASC:
                    return orderBy.asc(String.valueOf(value));
                case DESC:
                default:
                    return orderBy.desc(String.valueOf(value));
            }
        }

        return orderBy.getQuery();
    }
}
