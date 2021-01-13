package com.github.mrzhqiang.helper.data.domain;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class Chunk<T> implements Slice<T>, Serializable {

    private static final long serialVersionUID = -7854169361281698351L;


    private final List<T> content = Lists.newArrayList();
    private final Pageable pageable;

    /**
     * 分页类的构造方法。
     *
     * @param content  当前页的数据。不能为 Null。
     * @param pageable 分页信息。不能为 Null。
     */
    public Chunk(List<T> content, Pageable pageable) {
        Preconditions.checkNotNull(content, "content == null");
        Preconditions.checkNotNull(pageable, "pageable == null");
        this.content.addAll(content);
        this.pageable = pageable;
    }

    @Override
    public int getNumber() {
        return pageable.isPaged() ? pageable.getPageNumber() : 0;
    }

    @Override
    public int getSize() {
        return pageable.isPaged() ? pageable.getPageSize() : content.size();
    }

    @Override
    public int getNumberOfElements() {
        return content.size();
    }

    @Override
    public boolean hasPrevious() {
        return getNumber() > 0;
    }

    @Override
    public boolean isFirst() {
        return !hasPrevious();
    }

    @Override
    public boolean isLast() {
        return !hasNext();
    }

    @Override
    public Pageable nextPageable() {
        return hasNext() ? pageable.next() : Pageable.unpaged();
    }

    @Override
    public Pageable previousPageable() {
        return hasPrevious() ? pageable.previousOrFirst() : Pageable.unpaged();
    }

    @Override
    public boolean hasContent() {
        return !content.isEmpty();
    }

    @Override
    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    @Nonnull
    @Override
    public Iterator<T> iterator() {
        return content.iterator();
    }

    /**
     * 应用给定函数到分页内容。
     *
     * @param converter 必须不为 {@literal null}。
     */
    protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {
        Preconditions.checkNotNull(converter, "converter == null");
        return this.stream().map(converter).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Chunk<?> paging = (Chunk<?>) o;
        return Objects.equals(content, paging.content) && Objects.equals(pageable, paging.pageable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, pageable);
    }
}
