package com.github.mrzhqiang.helper.data.cassandra;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 持久化实体的 ID 接口。
 */
public interface MapId extends Map<String, Object> {

    /**
     * 快捷添加属性的方法。
     */
    MapId with(String name, @Nullable Object value);
}
