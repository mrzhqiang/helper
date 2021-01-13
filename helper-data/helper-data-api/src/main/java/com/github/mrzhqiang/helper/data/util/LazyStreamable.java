package com.github.mrzhqiang.helper.data.util;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 惰性实现的流能力。
 * <p>
 * 从给定的 {@link Supplier} 中加载 {@link Stream}。
 *
 * @param <T> 目标对象类型。
 */
final class LazyStreamable<T> implements Streamable<T> {
    private final Supplier<? extends Stream<T>> stream;

    private LazyStreamable(Supplier<? extends Stream<T>> stream) {
        this.stream = stream;
    }

    public static <T> LazyStreamable<T> of(Supplier<? extends Stream<T>> stream) {
        return new LazyStreamable<>(stream);
    }

    public Supplier<? extends Stream<T>> getStream() {
        return stream;
    }

    @Override
    public Iterator<T> iterator() {
        return stream().iterator();
    }

    @Override
    public Stream<T> stream() {
        return stream.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LazyStreamable<?> that = (LazyStreamable<?>) o;
        return Objects.equals(stream, that.stream);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stream);
    }

    @Override
    public String toString() {
        return "LazyStreamable(stream=" + this.getStream() + ")";
    }
}
