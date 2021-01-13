package com.github.mrzhqiang.helper.data.redis;

import lombok.Data;

import java.time.Instant;

/**
 * Redis 实体。
 * <p>
 * 鉴于 Redis 存储的基本是字符串，这里需要有一个实体抽象进行转换。
 *
 * @author mrzhqiang
 */
@Data
public abstract class RedisEntity {

    protected String id;
    protected Instant created;
    protected Instant modified;

}
