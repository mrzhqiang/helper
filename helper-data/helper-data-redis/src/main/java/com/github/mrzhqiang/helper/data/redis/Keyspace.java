package com.github.mrzhqiang.helper.data.redis;


import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Keyspace {

    /**
     * 命名空间名字，实际上是 Redis 的 Key 前缀，后面拼接实体的 key 字符串。
     */
    String value() default "";
}
