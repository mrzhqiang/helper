package com.github.mrzhqiang.helper.data.domain;

/**
 * 未分页的信息。
 */
enum UnPaged implements Pageable {

    INSTANCE;

    @Override
    public int getPageNumber() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPageSize() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pageable next() {
        return this;
    }

    @Override
    public Pageable previousOrFirst() {
        return this;
    }

    @Override
    public Pageable first() {
        return this;
    }

    @Override
    public boolean hasPrevious() {
        return false;
    }

    @Override
    public boolean isPaged() {
        return false;
    }
}
