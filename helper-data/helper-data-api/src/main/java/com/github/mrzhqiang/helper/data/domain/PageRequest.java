package com.github.mrzhqiang.helper.data.domain;

import com.github.mrzhqiang.helper.data.util.Datas;
import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;

/**
 * 分页请求。
 */
public class PageRequest implements Pageable, Serializable {

    private static final long serialVersionUID = 310056124124324620L;

    private final int page;
    private final int size;

    @SuppressWarnings("unused")
    public PageRequest() {
        this(0, 10);
    }

    protected PageRequest(int page, int size) {
        Preconditions.checkArgument(page >= 0, "page < 0");
        Preconditions.checkArgument(size > 0, "size < 1");
        this.page = Datas.pageNumber(page);
        this.size = Datas.pageSize(size);
    }

    /**
     * 创建分页请求的新实例。
     *
     * @param page 页面编码。从 0 开始计算。
     * @param size 页面大小。通常应控制在 20 -- 2000 之间。
     * @return 分页请求实例。
     */
    public static PageRequest of(int page, int size) {
        return new PageRequest(page, size);
    }

    @Override
    public int getPageNumber() {
        return page;
    }

    @Override
    public int getPageSize() {
        return size;
    }

    @Override
    public long getOffset() {
        return Datas.pageOffset(page, size);
    }

    @Override
    public boolean hasPrevious() {
        return page > 0;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable next() {
        return new PageRequest(getPageNumber() + 1, getPageSize());
    }

    public Pageable previous() {
        return getPageNumber() == 0 ? this : new PageRequest(getPageNumber() - 1, getPageSize());
    }

    @Override
    public Pageable first() {
        return new PageRequest(0, getPageSize());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PageRequest that = (PageRequest) o;
        return page == that.page && size == that.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(page, size);
    }
}
