package com.github.mrzhqiang.helper.data.redis;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.github.mrzhqiang.helper.time.DateTimes;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 本地日期时间转为长整型数字的序列化器。
 *
 * @see com.fasterxml.jackson.databind.annotation.JsonSerialize
 */
public final class LocalDateTimeToLongSerializer extends JsonSerializer<LocalDateTime> {

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        Long epochMilli = DateTimes.toEpochMilli(localDateTime);
        jsonGenerator.writeNumber(epochMilli);
    }

}
