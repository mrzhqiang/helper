package com.github.mrzhqiang.helper.data.util;

import com.google.common.base.Preconditions;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.collectingAndThen;

/**
 * Stream 能力接口。
 * <p>
 * 让对象支持 stream 功能，同时也支持 迭代器 功能。
 *
 * @author Oliver Gierke
 * @author Christoph Strobl
 */
@FunctionalInterface
public interface Streamable<T> extends Iterable<T>, Supplier<Stream<T>> {

    /**
     * 返回空的 {@link Streamable}。
     *
     * @return 将永不为 {@literal null}。
     */
    static <T> Streamable<T> empty() {
        return Collections::emptyIterator;
    }

    /**
     * 返回具有给定元素的 {@link Streamable}。
     *
     * @param t 返回的元素数组。
     */
    @SafeVarargs
    static <T> Streamable<T> of(T... t) {
        return () -> Arrays.asList(t).iterator();
    }

    /**
     * 对于给定的 {@link Iterable} 返回 {@link Streamable}。
     *
     * @param iterable 必须不为 {@literal null}。
     */
    static <T> Streamable<T> of(Iterable<T> iterable) {
        Preconditions.checkNotNull(iterable, "iterable == null");
        return iterable::iterator;
    }

    static <T> Streamable<T> of(Supplier<? extends Stream<T>> supplier) {
        return LazyStreamable.of(supplier);
    }

    /**
     * 创建底层 {@link Iterable} 的非并行 {@link Stream}。
     *
     * @return 将永不为 {@literal null}。
     */
    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * 返回新的 {@link Streamable}，它将给定 {@link Function} 应用到当前实例。
     *
     * @param mapper 必须不为 {@literal null}。
     * @see Stream#map(Function)
     */
    default <R> Streamable<R> map(Function<? super T, ? extends R> mapper) {
        Preconditions.checkNotNull(mapper, "mapper == null");
        return Streamable.of(() -> stream().map(mapper));
    }

    /**
     * 返回新的 {@link Streamable}，它将给定 {@link Function} 应用到当前实例。
     *
     * @param mapper 必须不为 {@literal null}。
     * @see Stream#flatMap(Function)
     */
    default <R> Streamable<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper) {
        Preconditions.checkNotNull(mapper, "mapper == null");
        return Streamable.of(() -> stream().flatMap(mapper));
    }

    /**
     * 返回新的 {@link Streamable}，它将给定过滤 {@link Predicate} 应用到当前实例。
     *
     * @param predicate 必须不为 {@literal null}。
     * @see Stream#filter(Predicate)
     */
    default Streamable<T> filter(Predicate<? super T> predicate) {
        Preconditions.checkNotNull(predicate, "predicate == null");
        return Streamable.of(() -> stream().filter(predicate));
    }

    /**
     * 判断当前 {@link Streamable} 是否为空.
     */
    default boolean isEmpty() {
        return !iterator().hasNext();
    }

    /**
     * 将当前实例和给定的 {@link Stream} 进行串联，创建新的 {@link Streamable}。
     *
     * @param stream 必须不为 {@literal null}。
     */
    default Streamable<T> and(Supplier<? extends Stream<? extends T>> stream) {
        Preconditions.checkNotNull(stream, "stream == null");
        return Streamable.of(() -> Stream.concat(this.stream(), stream.get()));
    }

    /**
     * 将当前实例和给定的值进行串联，创建新的 {@link Streamable}。
     *
     * @param others 必须不为 {@literal null}。
     * @return 将永不为 {@literal null}。
     */
    @SuppressWarnings("unchecked")
    default Streamable<T> and(T... others) {
        Preconditions.checkNotNull(others, "others == null");
        return Streamable.of(() -> Stream.concat(this.stream(), Arrays.stream(others)));
    }

    /**
     * 将当前实例和给定的 {@link Iterable} 进行串联，创建新的 {@link Streamable}。
     *
     * @param iterable 必须不为 {@literal null}。
     * @return 将永不为 {@literal null}。
     */
    default Streamable<T> and(Iterable<? extends T> iterable) {
        Preconditions.checkNotNull(iterable, "iterable == null");
        return Streamable.of(() -> Stream.concat(this.stream(), StreamSupport.stream(iterable.spliterator(), false)));
    }

    /**
     * 便捷方法。
     * <p>
     * 允许直接添加 {@link Streamable}，否则调用在 {@link #and(Iterable)} 和 {@link #and(Supplier)} 之间是不明确的。
     *
     * @param streamable 必须不为 {@literal null}。
     * @return 将永不为 {@literal null}。
     */
    default Streamable<T> and(Streamable<? extends T> streamable) {
        return and((Supplier<? extends Stream<? extends T>>) streamable);
    }

    /**
     * 创建一个新的不可修改的 {@link List}。
     *
     * @return 将永不为 {@literal null}。
     */
    default List<T> toList() {
        return stream().collect(toUnmodifiableList());
    }

    /**
     * 创建一个新的不可修改的 {@link Set}。
     *
     * @return 将永不为 {@literal null}。
     */
    default Set<T> toSet() {
        return stream().collect(toUnmodifiableSet());
    }

    /*
     * (non-Javadoc)
     * @see java.util.function.Supplier#get()
     */
    default Stream<T> get() {
        return stream();
    }

    /**
     * 返回一个 {@link Collector} 来创建一个不可修改的  {@link List}。
     *
     * @return 将永不为 {@literal null}。
     */
    static <T> Collector<T, ?, List<T>> toUnmodifiableList() {
        return collectingAndThen(Collectors.toList(), Collections::unmodifiableList);
    }

    /**
     * 返回一个 {@link Collector} 来创建一个不可修改的  {@link Set}。
     *
     * @return 将永不为 {@literal null}。
     */
    static <T> Collector<T, ?, Set<T>> toUnmodifiableSet() {
        return collectingAndThen(Collectors.toSet(), Collections::unmodifiableSet);
    }

    /**
     * 一个收集器。
     * <p>
     * 使用 {@link Collectors#toList} 作为中间收集器，可以轻松地从 {@link Stream} 生成 {@link Streamable}。
     *
     * @see #toStreamable(Collector)
     */
    static <S> Collector<S, ?, Streamable<S>> toStreamable() {
        return toStreamable(Collectors.toList());
    }

    /**
     * 一个可以从 {@link Stream} 和给定的中间收集器轻松生成 {@link Streamable} 的收集器。
     */
    @SuppressWarnings("unchecked")
    static <S, T extends Iterable<S>> Collector<S, ?, Streamable<S>> toStreamable(
            Collector<S, ?, T> intermediate) {

        return Collector.of( //
                (Supplier<T>) intermediate.supplier(), //
                (BiConsumer<T, S>) intermediate.accumulator(), //
                (BinaryOperator<T>) intermediate.combiner(), //
                Streamable::of);
    }
}
