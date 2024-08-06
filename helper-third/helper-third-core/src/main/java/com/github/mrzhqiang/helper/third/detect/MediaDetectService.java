package com.github.mrzhqiang.helper.third.detect;

import com.github.mrzhqiang.helper.CollectionUtils;

import java.util.List;

/**
 * 媒体检测服务。
 * <p>
 * 用于检测文本、图片以及视频，主要检测是否存在敏感信息，并提供采取后续措施的依据。
 */
public interface MediaDetectService {

    String SUPPLIER_TYPE_DI_TING = "DiTing";
    String SUPPLIER_TYPE_GOOGLE = "Google";
    String SUPPLIER_TYPE_OPEN_AI = "OpenAi";
    String SUPPLIER_TYPE_AZURE = "Azure";
    String SUPPLIER_TYPE_AMAZON_AWS = "AmazonAws";
    String SUPPLIER_TYPE_NEXT_DATA = "NextData";

    /**
     * 检测供应商类型是否支持。
     *
     * @param type 供应商类型字符串。
     * @return 返回 true 表示供应商类型受支持；否则表示类型不受支持，需要过滤掉。
     */
    default boolean isSupport(String type) {
        return false;
    }

    /**
     * 检测文本。
     *
     * @param request 媒体检测请求。
     * @return 媒体检测数据。如果为 null 表示请求出错或供应商不可用。
     */
    MediaDetectData detectText(MediaDetectTextRequest request);

    /**
     * 检测图片。
     *
     * @param request 媒体检测请求。
     * @return 媒体检测数据。如果为 null 表示请求出错或供应商不可用。
     */
    MediaDetectData detectImage(MediaDetectUrlRequest request);

    /**
     * 批量检测图片。
     *
     * @param requests 请求列表。
     * @return 媒体检测数据列表。
     */
    List<MediaDetectData> batchDetectImage(List<MediaDetectUrlRequest> requests);

    /**
     * 检测批量检测图片的结果。
     *
     * @param requests 图片检测请求。
     * @return 返回 true 表示检测通过，否则表示检测失败。
     */
    default boolean checkBatchDetectImage(List<MediaDetectUrlRequest> requests) {
        try {
            List<MediaDetectData> response = batchDetectImage(requests);
            if (!CollectionUtils.isEmpty(response)) {
                return response.stream().noneMatch(MediaDetectData::isBlock);
            }
        } catch (Exception ignored) {
            return false;
        }
        return true;
    }

    /**
     * 异步检测图片。
     *
     * @param requests 请求列表。
     * @return 异步的媒体检测数据。
     */
    MediaDetectAsyncData asyncDetectImage(List<MediaDetectUrlRequest> requests);

    /**
     * 查询异步图片检测结果。
     *
     * @param taskId 任务 ID。
     * @return 异步的媒体检测数据。
     */
    MediaDetectData queryAsyncDetectImage(String taskId);

    /**
     * 异步检测视频。
     *
     * @param request 媒体检测请求。
     * @return 异步的媒体检测数据。
     */
    MediaDetectAsyncData asyncDetectVideo(MediaDetectUrlRequest request);

    /**
     * 查询异步视频检测结果。
     *
     * @param taskId 任务 ID。
     * @return 异步的媒体检测数据。
     */
    MediaDetectData queryAsyncDetectVideo(String taskId);

}
