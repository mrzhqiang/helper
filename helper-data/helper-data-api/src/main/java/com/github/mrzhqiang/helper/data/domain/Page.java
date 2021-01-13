package com.github.mrzhqiang.helper.data.domain;

import java.util.Collections;
import java.util.function.Function;

/**
 * 页面是对象列表的子列表。
 * <p>
 * 它允许获取有关它在包含的整个列表中的位置的信息。
 *
 * @author Oliver Gierke
 */
public interface Page<T> extends Slice<T> {

    /**
     * 返回空的 {@link Page}。
     */
    static <T> Page<T> empty() {
        return empty(Pageable.unpaged());
    }

    /**
     * 通过给定的 {@link Pageable} 来创建一个新的空 {@link Page}。
     *
     * @param pageable 必须不为 {@literal null}.
     */
    static <T> Page<T> empty(Pageable pageable) {
        return Paging.of(Collections.emptyList(), pageable, 0);
    }

    /**
     * 获取总页数。
     */
    int getTotalPages();

    /**
     * 获取元素总数。
     */
    long getTotalElements();

    /**
     * 返回带有给定 {@link Function} 映射的当前分页内容的新分页。
     *
     * @param converter 必须不为 {@literal null}.
     */
    <U> Page<U> map(Function<? super T, ? extends U> converter);
}
