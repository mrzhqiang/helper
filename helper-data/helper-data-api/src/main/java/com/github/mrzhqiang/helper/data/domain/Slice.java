package com.github.mrzhqiang.helper.data.domain;

import com.github.mrzhqiang.helper.data.util.Streamable;

import java.util.List;
import java.util.function.Function;

/**
 * 切片数据。
 * <p>
 * 指示是否有下一个或前一个可用的数据片。
 * <p>
 * 允许获得一个 {@link Pageable} 来请求上一个或下一个 {@link Slice}。
 * <p>
 * 一般用在 Cassandra 数据库中，作为分页查询的数据返回。
 *
 * @author Oliver Gierke
 */
public interface Slice<T> extends Streamable<T> {

    /**
     * 获取当前切片的编号，非负数。
     */
    int getNumber();

    /**
     * 获取当前切片的大小。
     */
    int getSize();

    /**
     * 获取当前切片上的元素数量。
     */
    int getNumberOfElements();

    /**
     * 获取当前切片上的内容。
     */
    List<T> getContent();

    /**
     * 是否当前切片有内容。
     */
    boolean hasContent();

    /**
     * 判断当前切片是否为第一个。
     */
    boolean isFirst();

    /**
     * 判断当前切片是否为最后一个。
     */
    boolean isLast();

    /**
     * 是否有下一个切片。
     */
    boolean hasNext();

    /**
     * 是否有上一个切片。
     */
    boolean hasPrevious();

    /**
     * 获取用于请求当前切片的分页信息。
     */
    default Pageable getPageable() {
        return PageRequest.of(getNumber(), getSize());
    }

    /**
     * 返回用于请求下一个切片的分页信息。
     * <p>
     * 如果当前切片已经是最后一个，那么可以返回 {@link Pageable#unpaged()}，客户端应该在调用此方法之前检查 {@link #hasNext()}。
     *
     * @see #nextOrLastPageable()
     */
    Pageable nextPageable();

    /**
     * 返回用于请求上一个切片的分页信息。
     * <p>
     * 如果当前切片已经是第一个，那么可以返回 {@link Pageable#unpaged()}。客户端应该在调用此方法之前检查 {@link #hasPrevious()}。
     *
     * @see #previousPageable()
     */
    Pageable previousPageable();

    /**
     * 返回带有给定 {@link Function} 映射的当前切片内容的新切片。
     *
     * @param converter 必须不为 {@literal null}.
     */
    <U> Slice<U> map(Function<? super T, ? extends U> converter);

    /**
     * 返回描述下一个切片的 {@link Pageable} 或描述当前切片的那个（如果它是最后一个切片）。
     */
    default Pageable nextOrLastPageable() {
        return hasNext() ? nextPageable() : getPageable();
    }

    /**
     * 返回描述上一个切片的 {@link Pageable} 或描述当前切片的那个（如果它是最后一个切片）。
     */
    default Pageable previousOrFirstPageable() {
        return hasPrevious() ? previousPageable() : getPageable();
    }
}
