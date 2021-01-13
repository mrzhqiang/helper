package com.github.mrzhqiang.helper.data.domain;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 分片的默认实现。
 */
public class Slicing<T> extends Chunk<T> {

    private static final long serialVersionUID = -8183360178587588310L;

    private final boolean hasNext;
    private final Pageable pageable;

    /**
     * 分片的构造方法。
     *
     * @param content  当前页的数据。不能为 Null。
     * @param pageable 分页信息。不能为 Null。
     * @param hasNext  当前分片是否还有下一个分片。
     */
    private Slicing(List<T> content, Pageable pageable, boolean hasNext) {
        super(content, pageable);
        this.hasNext = hasNext;
        this.pageable = pageable;
    }

    /**
     * 分片的构造方法。
     *
     * @param content 当前页的数据。不能为 Null。
     */
    private Slicing(List<T> content) {
        this(content, Pageable.unpaged(), false);
    }

    public static <T> Slice<T> of(List<T> content, Pageable pageable, boolean hasNext) {
        return new Slicing<>(content, pageable, hasNext);
    }

    public static <T> Slice<T> of(List<T> content) {
        return new Slicing<>(content);
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public <U> Slice<U> map(Function<? super T, ? extends U> converter) {
        return new Slicing<>(getConvertedContent(converter), pageable, hasNext);
    }

    @Override
    public String toString() {
        String contentType = "UNKNOWN";
        List<T> content = getContent();

        if (content.size() > 0) {
            contentType = content.get(0).getClass().getName();
        }

        return String.format("Slice %d containing %s instances", getNumber(), contentType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Slicing<?> slicing = (Slicing<?>) o;
        return hasNext == slicing.hasNext;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hasNext);
    }
}
