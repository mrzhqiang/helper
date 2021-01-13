package com.github.mrzhqiang.helper.data.repository;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 增删改查存储库。
 * <p>
 * 定义通用的 CRUD 操作。
 *
 * @author mrzhqiang
 */
public interface CrudRepository<T, ID> extends Repository<T, ID> {

    /**
     * 保存实体。
     *
     * @param entity 实体，不允许是 Null 值。
     * @return 已保存的实体，因为保存过程中可能会修改实体（比如生成 ID，更新修改时间等），不为 Null 值。
     * @throws IllegalArgumentException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    @Nullable
    T save(T entity);

    /**
     * 保存所有实体。
     *
     * @param entities 不允许是 Null 值，并且不能包含 Null 元素。
     * @return 已保存的实体集合，因为保存过程中可能会修改实体，不为 Null 值。并且数量与传入大小保持一致。
     * @throws IllegalArgumentException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    Iterable<T> saveAll(Iterable<T> entities);

    /**
     * 通过 ID 检索实体。
     *
     * @param id 不允许是 Null 值。
     * @return 具有 id 属性的实体，或者是一个 Optional.empty 值。
     * @throws IllegalArgumentException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    Optional<T> findById(ID id);

    /**
     * 通过 ID 迭代器检索相关数据。
     *
     * @return 包含相关数据的实体迭代器。
     * @throws IllegalArgumentException 如果参数为 Null 或包含 Null 元素，则抛出非法参数异常。
     */
    Iterable<T> findAllById(Iterable<ID> ids);

    /**
     * 通过 ID 判断是否存在该数据。
     *
     * @throws IllegalArgumentException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    boolean existsById(ID id);

    /**
     * 检索全量数据。
     * <p>
     * 在数据量特别大的情况下，不建议使用这个方法，会造成一次慢查询。
     *
     * @return 包含全部数据的实体迭代器。
     */
    Iterable<T> findAll();

    /**
     * 总数量。
     * <p>
     * 这个方法查询出来的数据，属于不包含查询条件的全部数量。
     */
    long count();

    /**
     * 通过主键删除数据。
     *
     * @throws IllegalArgumentException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    void deleteById(ID id);

    /**
     * 通过实体删除数据。
     *
     * @throws IllegalArgumentException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    void delete(T entity);

    /**
     * 通过实体迭代器删除相关数据。
     *
     * @param entities 不允许是 null 值，并且不能包含 null 元素。
     * @throws IllegalArgumentException 如果参数为 null 或包含 null 元素，则抛出非法参数异常。
     */
    void deleteAll(Iterable<T> entities);

    /**
     * 删除所有实体数据。
     */
    void deleteAll();
}
