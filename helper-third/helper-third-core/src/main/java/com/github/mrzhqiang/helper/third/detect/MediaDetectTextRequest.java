package com.github.mrzhqiang.helper.third.detect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 媒体检测文本请求。
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MediaDetectTextRequest {

    /**
     * 数据 ID。
     */
    private String dataId;
    /**
     * 文本内容。
     */
    private String text;
    /**
     * 文本类型。
     */
    private String textType;

}
