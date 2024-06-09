package com.github.mrzhqiang.helper.data.redis;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.github.mrzhqiang.helper.time.DateTimes;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * 长整型数字转为本地日期时间的反序列化器。
 *
 * @see com.fasterxml.jackson.databind.annotation.JsonDeserialize
 */
public final class LongToLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        Long epochMilli = jsonParser.getValueAsLong();
        Instant instant = DateTimes.ofEpochMilli(epochMilli);
        if (instant == null) {
            return null;
        }
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

}
