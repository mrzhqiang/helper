package com.github.mrzhqiang.helper.data.cassandra;

import com.google.common.base.Preconditions;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BasicMapId implements MapId {

    private final Map<String, Object> map = new HashMap<>();

    public BasicMapId() {
    }

    public BasicMapId(Map<String, Object> map) {
        Preconditions.checkArgument(Objects.nonNull(map), "Map must not be null");
        this.map.putAll(map);
    }

    public static MapId id() {
        return new BasicMapId();
    }

    public static MapId id(String name, Object value) {
        return new BasicMapId().with(name, value);
    }

    public static MapId id(MapId id) {
        return new BasicMapId(id);
    }

    @Override
    public BasicMapId with(String name, @Nullable Object value) {
        put(name, value);
        return this;
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public boolean containsKey(Object name) {
        return map.containsKey(name);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Nonnull
    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (!(that instanceof Map)) { // we can be equal to a Map
            return false;
        }
        return map.equals(that);
    }

    @Override
    public Object get(Object name) {
        return map.get(name);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Nonnull
    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Object put(String name, Object value) {
        return map.put(name, value);
    }

    @Override
    public void putAll(@Nonnull Map<? extends String, ?> source) {
        map.putAll(source);
    }

    @Override
    public Object remove(Object name) {
        return map.remove(name);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Nonnull
    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public String toString() {

        StringBuilder s = new StringBuilder("{ ");

        boolean first = true;
        for (Map.Entry<String, Object> entry : map.entrySet()) {

            if (first) {
                first = false;
            } else {
                s.append(", ");
            }

            s.append(entry.getKey()).append(" : ").append(entry.getValue());
        }

        return s.append(" }").toString();
    }
}
