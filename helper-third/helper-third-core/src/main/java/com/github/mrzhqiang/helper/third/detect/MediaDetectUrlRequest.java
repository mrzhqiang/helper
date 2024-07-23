package com.github.mrzhqiang.helper.third.detect;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 媒体检测 URL 请求。
 * <p>
 * 可能是图片、音频、视频等链接。
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MediaDetectUrlRequest {

    /**
     * 数据 ID。
     */
    private String dataId;
    /**
     * URL 链接。
     */
    private String url;

    /**
     * 用户 ID。
     * <p>
     * 看旧代码，只是为了打印日志。
     */
    private String userId;

    private String mediaId;

}
