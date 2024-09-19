package com.github.mrzhqiang.helper;

import java.util.Collection;
import java.util.Map;

/**
 * 集合工具。
 * <p>
 * 模仿 Spring Framework 生成，后续不需要则删除。
 */
public final class CollectionUtils {

    private CollectionUtils() {
        // no instances.
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

}
