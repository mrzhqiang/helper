package com.github.mrzhqiang.helper.third.detect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异步的媒体检测数据。
 * <p>
 * 异步数据返回任务 ID，之后可以通过任务 ID 去获取结果。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MediaDetectAsyncData {

    private String taskId;
    private String msg;

    private MediaDetectData data;

}
