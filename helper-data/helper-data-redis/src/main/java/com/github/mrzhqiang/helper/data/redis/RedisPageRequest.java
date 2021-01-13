package com.github.mrzhqiang.helper.data.redis;

import com.github.mrzhqiang.helper.data.domain.PageRequest;
import com.github.mrzhqiang.helper.data.domain.Pageable;
import com.google.common.base.Preconditions;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.annotation.Nullable;
import java.util.Objects;

@Data
@EqualsAndHashCode(callSuper = true)
public class RedisPageRequest extends PageRequest {

    private static final long serialVersionUID = 8269155373321353390L;

    @Nullable
    private final String cursor;
    private final boolean nextAllowed;


    protected RedisPageRequest(int page, int size, @Nullable String cursor, boolean nextAllowed) {
        super(page, size);
        this.cursor = cursor;
        this.nextAllowed = nextAllowed;
    }

    public static RedisPageRequest of(int page, int size) {
        Preconditions.checkArgument(page == 0, "page != 0");

        return new RedisPageRequest(page, size, null, false);
    }

    public static RedisPageRequest of(Pageable current, @Nullable String cursor) {
        return new RedisPageRequest(current.getPageNumber(), current.getPageSize(), cursor, Objects.nonNull(cursor));
    }

    public static RedisPageRequest first(int size) {
        return new RedisPageRequest(0, size, null, false);
    }

    public static void validatePageable(Pageable pageable) {
        if (pageable.isUnpaged() || pageable.getPageNumber() == 0) {
            return;
        }

        if (pageable instanceof RedisPageRequest) {
            RedisPageRequest pageRequest = (RedisPageRequest) pageable;
            if (Objects.nonNull(pageRequest.getCursor())) {
                return;
            }
        }

        throw new IllegalArgumentException(
                "Paging queries for pages other than the first one require a RedisPageRequest with a valid paging state");
    }

    @Nullable
    public String getCursor() {
        return cursor;
    }

    public boolean hasNext() {
        return Objects.nonNull(getCursor()) && this.nextAllowed;
    }

    @Override
    public RedisPageRequest next() {
        Preconditions.checkState(hasNext(), "Cannot create a next page request without a PagingState");

        return new RedisPageRequest(getPageNumber() + 1, getPageSize(), this.cursor, false);
    }

    @Override
    public Pageable previous() {
        Preconditions.checkState(getPageNumber() < 2, "Cannot navigate to an intermediate page");

        return super.previous();
    }
}
