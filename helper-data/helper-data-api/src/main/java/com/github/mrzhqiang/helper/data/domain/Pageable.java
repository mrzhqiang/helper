package com.github.mrzhqiang.helper.data.domain;

import java.util.Optional;

/**
 * 分页信息接口。
 */
public interface Pageable {

    /**
     * 返回不包含分页信息的实例。
     */
    static Pageable unpaged() {
        return UnPaged.INSTANCE;
    }

    /**
     * 判断是否包含分页信息。
     */
    default boolean isPaged() {
        return true;
    }

    /**
     * 判断是否不包含分页信息。
     */
    default boolean isUnpaged() {
        return !isPaged();
    }

    /**
     * 返回当前页的页面编号，从 0 开始。
     */
    int getPageNumber();

    /**
     * 返回当前页的页面大小，一页默认为 20 行数据。
     */
    int getPageSize();

    /**
     * 返回当前页的偏移量，根据基础页面和页面大小进行计算。
     * <p>
     * 通常用来作为 limit 查询。
     * <p>
     * 某种情况下，比如数据存在重复行，则需要添加创建时间进行排序，以免数据错乱。
     */
    long getOffset();

    /**
     * 返回下一页的分页信息。
     */
    Pageable next();

    /**
     * 返回上一页的页面信息或者首页的页面信息。
     */
    Pageable previousOrFirst();

    /**
     * 返回首页的页面信息。
     */
    Pageable first();

    /**
     * 判断是否存在上一页的页面信息。
     */
    boolean hasPrevious();

    /**
     * 返回分页信息的可选类包装实例。
     */
    default Optional<Pageable> optional() {
        return isUnpaged() ? Optional.empty() : Optional.of(this);
    }
}
