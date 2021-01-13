package com.github.mrzhqiang.helper.data.cassandra;

import com.datastax.driver.core.PagingState;
import com.github.mrzhqiang.helper.data.domain.PageRequest;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class CassandraPageRequest extends PageRequest {

    private static final long serialVersionUID = 4300248065545168831L;

    @Nullable
    private final String pagingState;
    private final boolean nextAllowed;

    protected CassandraPageRequest(int page, int size, @Nullable PagingState pagingState, boolean nextAllowed) {
        super(page, size);
        this.pagingState = Objects.isNull(pagingState) ? null : pagingState.toString();
        this.nextAllowed = nextAllowed;
    }

    public static CassandraPageRequest of(int page, int size) {
        Preconditions.checkArgument(page == 0, "page != 0");
        return new CassandraPageRequest(page, size, null, false);
    }

    public static CassandraPageRequest of(Pageable current, @Nullable PagingState pagingState) {
        return new CassandraPageRequest(current.getPageNumber(), current.getPageSize(), pagingState, Objects.nonNull(pagingState));
    }

    public static CassandraPageRequest first(int size) {
        return new CassandraPageRequest(0, size, null, false);
    }

    public static void validatePageable(Pageable pageable) {
        if (pageable.isUnpaged() || pageable.getPageNumber() == 0) {
            return;
        }

        if (pageable instanceof CassandraPageRequest) {
            CassandraPageRequest pageRequest = (CassandraPageRequest) pageable;
            if (Objects.nonNull(pageRequest.getPagingState())) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "Paging queries for pages other than the first one require a CassandraPageRequest with a valid paging state");
    }

    @Nullable
    public PagingState getPagingState() {
        return Objects.isNull(pagingState) ? null : PagingState.fromString(pagingState);
    }

    public boolean hasNext() {
        return Objects.nonNull(getPagingState()) && this.nextAllowed;
    }

    @Override
    public CassandraPageRequest next() {
        Preconditions.checkState(hasNext(), "Cannot create a next page request without a PagingState");
        return new CassandraPageRequest(getPageNumber() + 1, getPageSize(), getPagingState(), false);
    }

    @Override
    public Pageable previous() {
        Preconditions.checkState(getPageNumber() < 2, "Cannot navigate to an intermediate page");
        return super.previous();
    }
}
