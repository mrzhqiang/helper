package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.CollectionUtils;
import com.github.mrzhqiang.helper.StringUtils;
import com.github.mrzhqiang.helper.third.detect.MediaDetectAsyncData;
import com.github.mrzhqiang.helper.third.detect.MediaDetectData;
import com.github.mrzhqiang.helper.third.detect.MediaDetectFailureException;
import com.github.mrzhqiang.helper.third.detect.MediaDetectServerException;
import com.github.mrzhqiang.helper.third.detect.MediaDetectService;
import com.github.mrzhqiang.helper.third.detect.MediaDetectTextRequest;
import com.github.mrzhqiang.helper.third.detect.MediaDetectUrlRequest;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataImageData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataQueryData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataVideoBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataVideoData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.BatchNextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.BatchNextDataImageData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataResultData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataResultVideoData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataTextBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataVideoBody;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Next Data 媒体检测服务的实现。
 */
@Slf4j
@RequiredArgsConstructor
public class NextDataMediaDetectService implements MediaDetectService {

    private static final String NICKNAME_TYPE = "nick";

    private final NextDataApiProperties properties;
    private final NextDataTextApi textApi;
    private final NextDataImageApi imageApi;
    private final NextDataAsyncImageApi asyncImageApi;
    private final NextDataVideoApi videoApi;
    private final NextDataQueryApi queryApi;

    @Override
    public boolean isSupport(String type) {
        return SUPPLIER_TYPE_NEXT_DATA.equals(type);
    }

    @Override
    public MediaDetectData detectText(MediaDetectTextRequest request) {
        NextDataTextBody.Data data = NextDataTextBody.Data.builder()
                .text(request.getText())
                .tokenId(request.getDataId())
                .build();
        NextDataTextBody textBody = NextDataTextBody.builder()
                .accessKey(properties.getAccessKey())
                .data(data)
                .build();
        if (NICKNAME_TYPE.equals(request.getTextType())) {
            textBody.setEventId(NextDataTextBody.NICKNAME_EVENT_ID);
        }
        Call<NextDataResultData> call = textApi.detectText(textBody);
        try {
            Response<NextDataResultData> execute = call.execute();
            if (execute.isSuccessful()) {
                NextDataResultData body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (!body.isSuccess()) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return MediaDetectData.builder()
                        .suggestion(body.getRiskLevel())
                        .label(body.getRiskDescription())
                        .dataId(request.getDataId())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("next data detect text failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData detectImage(MediaDetectUrlRequest request) {
        Call<NextDataResultData> call = imageApi.detectImage(NextDataImageBody.builder()
                .accessKey(properties.getAccessKey())
                .data(NextDataImageBody.ImageData.builder()
                        .img(request.getUrl())
                        .tokenId(request.getDataId())
                        .build())
                .build());
        try {
            Response<NextDataResultData> execute = call.execute();
            if (execute.isSuccessful()) {
                NextDataResultData body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (!body.isSuccess()) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return MediaDetectData.builder()
                        .label(body.getRiskLabel1())
                        .suggestion(body.getRiskLevel())
                        .dataId(request.getDataId())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("next data detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public List<MediaDetectData> batchDetectImage(List<MediaDetectUrlRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            return Collections.emptyList();
        }

        Map<String, MediaDetectUrlRequest> cache = requests.stream()
                .collect(Collectors.toMap(it -> it.getUserId() + it.getDataId(), Function.identity(), (o, o2) -> o));
        // TODO use MapStruct replace here code and BeanUtils.copyProperties
        List<BatchNextDataImageBody.ImageData> imgs = requests.stream()
                .map(it -> BatchNextDataImageBody.ImageData.builder()
                        .img(it.getUrl())
                        .btId(it.getUserId() + it.getDataId())
                        .build())
                .collect(Collectors.toList());
        String tokenId = requests.stream()
                .map(MediaDetectUrlRequest::getUserId)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse("");
        Call<BatchNextDataImageData> call = imageApi.batchDetectImage(BatchNextDataImageBody.builder()
                .accessKey(properties.getAccessKey())
                .data(BatchNextDataImageBody.BatchData.builder()
                        .imgs(imgs)
                        .tokenId(tokenId)
                        .build())
                .build());
        try {
            Response<BatchNextDataImageData> execute = call.execute();
            if (execute.isSuccessful()) {
                BatchNextDataImageData body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (!body.isSuccess() || CollectionUtils.isEmpty(body.getImgs())) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return body.getImgs().stream()
                        .map(it -> MediaDetectData.builder()
                                .dataId(it.getBtId())
                                .suggestion(it.getRiskLevel())
                                .label(it.getRiskLabel1())
                                .userId(tokenId)
                                .mediaId(cache.get(it.getBtId()).getMediaId())
                                .build())
                        .collect(Collectors.toList());
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("next data batch detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    /**
     * 异步识别图片。
     * <p>
     * 注意：NextData 的图片异步请求仅支持单张图片请求，且硅谷接口提交的异步图片请求不支持异步结果查询。
     *
     * @param requests 请求列表。
     * @return 媒体检测的异步数据。
     */
    @Override
    public MediaDetectAsyncData asyncDetectImage(List<MediaDetectUrlRequest> requests) {
        if (CollectionUtils.isEmpty(requests)) {
            throw new RuntimeException();
        }
        log.info("async detect image requests: {}", requests);
        // NextData 不支持多张图片的异步请求
        if (requests.size() > 1) {
            log.error("async detect image request cannot exceed one picture!");
            throw new RuntimeException();
        }
        MediaDetectUrlRequest request = requests.get(0);
        String tokenId = request.getDataId();
        NextDataImageBody.ImageData img = NextDataImageBody.ImageData.builder()
                .img(request.getUrl())
                .tokenId(tokenId)
                .build();
        Call<AsyncNextDataImageData> call = asyncImageApi.asyncDetectImage(NextDataImageBody.builder()
                .accessKey(properties.getAccessKey())
                .data(img)
                .build());
        try {
            Response<AsyncNextDataImageData> execute = call.execute();
            if (execute.isSuccessful()) {
                AsyncNextDataImageData body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (!body.isSuccess()) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                String requestId = body.getRequestId();
                return MediaDetectAsyncData.builder()
                        .taskId(requestId)
                        .msg(body.getMessage())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("next data async detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData queryAsyncDetectImage(String taskId) {
        Call<AsyncNextDataQueryData> call = queryApi.queryImage(AsyncNextDataImageBody.builder()
                .accessKey(properties.getAccessKey())
                .requestIds(Lists.newArrayList(AsyncNextDataImageBody.RequestId.builder()
                        .requestId(taskId)
                        .build()))
                .build());
        try {
            Response<AsyncNextDataQueryData> execute = call.execute();
            if (execute.isSuccessful()) {
                AsyncNextDataQueryData body = execute.body();
                // 如果是空数据，或者检测中，我们返回 null 记录一下检测次数
                if (body == null || body.isHandling()) {
                    return null;
                }
                if (!body.isSuccess() || CollectionUtils.isEmpty(body.getContents())) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return body.getContents().stream()
                        .filter(AsyncNextDataQueryData.Content::isCompleted)
                        .map(AsyncNextDataQueryData.Content::getResult)
                        .map(it -> MediaDetectData.builder()
                                .suggestion(it.getRiskLevel())
                                .dataId(it.getRequestId())
                                .label(it.getRiskLabel1())
                                .build())
                        .findFirst()
                        .orElse(null);
            }
            // 这里可能不是成功的 http 请求，需要返回 null 表示查询失败，而不是检测失败或者服务异常
            return null;
        } catch (IOException e) {
            log.error("next data query async detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectAsyncData asyncDetectVideo(MediaDetectUrlRequest request) {
        Call<NextDataResultVideoData> call = videoApi.detectVideo(NextDataVideoBody.builder()
                .accessKey(properties.getAccessKey())
                .data(NextDataVideoBody.VideoData.builder()
                        .btId(request.getUserId() + request.getDataId())
                        .url(request.getUrl())
                        .tokenId(request.getDataId())
                        .build())
                .build());
        try {
            Response<NextDataResultVideoData> execute = call.execute();
            if (execute.isSuccessful()) {
                NextDataResultVideoData body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (!body.isSuccess()) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return MediaDetectAsyncData.builder()
                        .taskId(body.getBtId())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("next data async detect video failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData queryAsyncDetectVideo(String taskId) {
        Call<AsyncNextDataVideoData> call = videoApi.queryVideo(AsyncNextDataVideoBody.builder()
                .accessKey(properties.getAccessKey())
                .btId(taskId)
                .build());
        try {
            Response<AsyncNextDataVideoData> execute = call.execute();
            if (execute.isSuccessful()) {
                AsyncNextDataVideoData body = execute.body();
                // 如果是空数据，或者检测中，我们返回 null 记录一下检测次数
                if (body == null || body.isHandling()) {
                    return null;
                }
                if (!body.isSuccess()) {
                    throw new MediaDetectFailureException(body.getMessage());
                }
                return MediaDetectData.builder()
                        .dataId(body.getBtId())
                        .suggestion(body.getRiskLevel())
                        .build();
            }
            // 这里可能不是成功的 http 请求，需要返回 null 表示查询失败，而不是检测失败或者服务异常
            return null;
        } catch (IOException e) {
            log.error("next data query async detect video failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

}
