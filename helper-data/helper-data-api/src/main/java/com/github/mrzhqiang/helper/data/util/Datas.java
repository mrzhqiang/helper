package com.github.mrzhqiang.helper.data.util;

import com.github.mrzhqiang.helper.data.DataAccessException;
import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 数据库工具。
 *
 * @author qiang.zhang
 */
public final class Datas {
    private Datas() {
        // no instances
    }

    private static final int DEFAULT_MIN_PAGE_NUMBER = 0;
    private static final int DEFAULT_MIN_PAGE_SIZE = 10;
    private static final int DEFAULT_MAX_PAGE_SIZE = 2000;

    /**
     * 计算页面编号。
     *
     * @param page 页面编号从 0 开始，不封顶。
     * @return 页面编号，非负数。
     */
    public static int pageNumber(int page) {
        return Math.max(DEFAULT_MIN_PAGE_NUMBER, page);
    }

    /**
     * 计算页面的最大行数。
     *
     * @param size 页面大小。
     * @return 最大行数。最小值为 10，最大值为 2000。这是为了防止程序被恶意破坏。
     */
    public static int pageSize(int size) {
        return Math.min(DEFAULT_MAX_PAGE_SIZE, Math.max(DEFAULT_MIN_PAGE_SIZE, size));
    }

    /**
     * 计算页面的首行序号。
     *
     * @param number 页面索引。
     * @param size   页面最大行数。
     * @return 首行序号。最小值为 0。
     */
    public static int pageOffset(int number, int size) {
        return Math.max(0, number * size);
    }

    /**
     * 计算页面的总数。
     *
     * @param total 总行数。
     * @param size  页面最大行数。
     * @return 页面数量。
     */
    public static int pageTotal(long total, int size) {
        if (total <= 0) {
            return 0;
        } else {
            return (int) (((total - 1) / size) + 1);
        }
    }

    /**
     * 测试指定对象的断言。
     *
     * @param target    目标对象。
     * @param predicate 断言对象。
     * @param <T>       目标类型。
     * @return true 检测通过；false 检测不通过。
     * @throws DataAccessException 数据库访问异常，用来统一数据异常类型，方便上层捕捉。
     */
    public static <T> boolean test(T target, Predicate<T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate == null");
        try {
            return predicate.test(target);
        } catch (Exception e) {
            throw new DataAccessException("data check failed.", e);
        }
    }

    /**
     * 获取供应商的实例。
     *
     * @param supplier 供应商对象。
     * @param <T>      目标类型。
     * @return 获取的目标对象，不允许为 Null。
     * @throws DataAccessException 数据库访问异常，用来统一数据异常类型，方便上层捕捉。
     */
    public static <T> T get(Supplier<T> supplier) {
        Preconditions.checkNotNull(supplier, "supplier == null");
        try {
            return Objects.requireNonNull(supplier.get());
        } catch (Exception e) {
            throw new DataAccessException("data get failed.", e);
        }
    }

    /**
     * 通过消费者接受指定对象。
     *
     * @param target   目标对象。
     * @param <T>      目标类型。
     * @param consumer 消费者对象。
     * @throws DataAccessException 数据库访问异常，用来统一数据异常类型，方便上层捕捉。
     */
    public static <T> void accept(T target, Consumer<T> consumer) {
        Preconditions.checkNotNull(target, "target == null");
        Preconditions.checkNotNull(consumer, "consumer == null");
        try {
            consumer.accept(target);
        } catch (Exception e) {
            throw new DataAccessException("data accept failed.", e);
        }
    }

    /**
     * 通过函数转换输入对象为其他对象。
     *
     * @param input    输入对象。
     * @param function 函数对象。
     * @param <I>      输入类型。
     * @param <R>      返回类型。
     * @return 返回对象，可能为 Null。
     * @throws DataAccessException 数据库访问异常，用来统一数据异常类型，方便上层捕捉。
     */
    @Nullable
    public static <I, R> R apply(I input, Function<I, R> function) {
        Preconditions.checkNotNull(input, "input == null");
        Preconditions.checkNotNull(function, "function == null");
        try {
            return function.apply(input);
        } catch (Exception e) {
            throw new DataAccessException("data apply failed.", e);
        }
    }
}
