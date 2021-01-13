package com.github.mrzhqiang.helper.data.domain;

import com.github.mrzhqiang.helper.data.util.Datas;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 分页数据。
 *
 * @param <T> 数据类型。
 * @author mrzhqiang
 */
public class Paging<T> extends Chunk<T> implements Page<T> {

    private static final long serialVersionUID = -8862553635128277132L;

    private final long total;

    /**
     * 分页类的构造方法。
     *
     * @param content  当前页的数据。不能为 Null。
     * @param pageable 分页信息。不能为 Null。
     * @param total    数据的总数。
     */
    private Paging(List<T> content, Pageable pageable, long total) {
        super(content, pageable);
        this.total = pageable.optional()
                .filter(it -> !content.isEmpty())
                .filter(it -> it.getOffset() + it.getPageSize() > total)
                .map(it -> it.getOffset() + content.size())
                .orElse(total);
    }

    /**
     * 分页类的构造方法。
     *
     * @param content 当前页的数据。不能为 Null。
     */
    private Paging(List<T> content) {
        this(content, Pageable.unpaged(), content.size());
    }

    public static <T> Paging<T> of(List<T> content, Pageable pageable, long total) {
        return new Paging<>(content, pageable, total);
    }

    public static <T> Paging<T> of(List<T> content) {
        return new Paging<>(null == content ? Collections.emptyList() : content);
    }

    public static <T> Paging<T> ofEmpty() {
        return of(Collections.emptyList());
    }

    @Override
    public int getTotalPages() {
        return getSize() == 0 ? 1 : Datas.pageTotal(total, getSize());
    }

    @Override
    public long getTotalElements() {
        return total;
    }

    @Override
    public boolean hasNext() {
        return getNumber() + 1 < getTotalPages();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public <U> Page<U> map(Function<? super T, ? extends U> converter) {
        return new Paging<>(getConvertedContent(converter), getPageable(), total);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Paging<?> paging = (Paging<?>) o;
        return total == paging.total;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), total);
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (!content.isEmpty() && content.get(0) != null) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Page %s of %d containing %s instances", getNumber() + 1, getTotalPages(), contentType);
    }
}
