package com.github.mrzhqiang.helper.third.detect.diting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mrzhqiang.helper.StringUtils;
import com.github.mrzhqiang.helper.third.detect.MediaDetectAsyncData;
import com.github.mrzhqiang.helper.third.detect.MediaDetectData;
import com.github.mrzhqiang.helper.third.detect.MediaDetectFailureException;
import com.github.mrzhqiang.helper.third.detect.MediaDetectServerException;
import com.github.mrzhqiang.helper.third.detect.MediaDetectService;
import com.github.mrzhqiang.helper.third.detect.MediaDetectTextRequest;
import com.github.mrzhqiang.helper.third.detect.MediaDetectUrlRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectRequestTask;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectResponseResults;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectResultResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.InspectAsyncResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.InspectAsyncResultRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.TextInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.TextInspectResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.VideoInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.VideoInspectResultResponse;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * DiTing 媒体检测服务的实现。
 */
@Slf4j
@RequiredArgsConstructor
public class DiTingMediaDetectService implements MediaDetectService {

    private static final String DEF_TEXT_TYPE = "post";

    private final DiTingApiProperties properties;
    private final DiTingApi diTingApi;
    private final DiTingTextApi diTingTextApi;

    @Override
    public boolean isSupport(String type) {
        return SUPPLIER_TYPE_DI_TING.equals(type);
    }

    @Override
    public MediaDetectData detectText(MediaDetectTextRequest request) {
        if (!StringUtils.hasText(request.getTextType())) {
            request.setTextType(DEF_TEXT_TYPE);
        }
        Call<TextInspectResponse> call = diTingTextApi.detectText(TextInspectRequest.builder()
                .token(properties.getToken())
                .context(request.getText())
                .context_type(request.getTextType())
                .data_id(request.getDataId())
                .build());
        try {
            Response<TextInspectResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                TextInspectResponse body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (body.getCode() != 0 || body.getData() == null) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return MediaDetectData.builder()
                        .suggestion(body.getData().getSuggestion())
                        .label(body.getData().getLabel())
                        .dataId(body.getData_id())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("di ting detect text failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData detectImage(MediaDetectUrlRequest request) {
        Call<ImageInspectResponse> call = diTingApi.inspectImage(ImageInspectRequest.builder()
                .token(properties.getToken())
                .scenes(properties.getImageScenes())
                .tasks(ImmutableList.of(ImageInspectRequestTask.builder()
                        .url(request.getUrl())
                        .data_id(request.getDataId())
                        .user_id(request.getUserId())
                        .build()))
                .build());
        try {
            Response<ImageInspectResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                ImageInspectResponse body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                List<ImageInspectResponseResults> results = body.getResults();
                if (results == null || results.isEmpty() || results.get(0).getCode() != 200) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return MediaDetectData.builder()
                        .label(results.get(0).getLabel())
                        .suggestion(results.get(0).getSuggestion())
                        .dataId(results.get(0).getData_id())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("di ting detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public List<MediaDetectData> batchDetectImage(List<MediaDetectUrlRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException();
        }

        Map<String, MediaDetectUrlRequest> cache = requests.stream()
                .collect(Collectors.toMap(MediaDetectUrlRequest::getDataId, Function.identity(), (o, o2) -> o));

        // TODO use MapStruct replace here code and BeanUtils.copyProperties
        List<ImageInspectRequestTask> tasks = requests.stream()
                .map(it -> ImageInspectRequestTask.builder()
                        .url(it.getUrl())
                        .data_id(it.getDataId())
                        .user_id(it.getUserId())
                        .build())
                .collect(Collectors.toList());
        Call<ImageInspectResponse> call = diTingApi.inspectImage(ImageInspectRequest.builder()
                .token(properties.getToken())
                .scenes(properties.getImageScenes())
                .tasks(tasks)
                .build());
        try {
            Response<ImageInspectResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                ImageInspectResponse body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (body.getCode() != 200 || body.getResults() == null || body.getResults().isEmpty()) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return body.getResults().stream()
                        .map(it -> MediaDetectData.builder()
                                .dataId(it.getData_id())
                                .suggestion(it.getSuggestion())
                                .label(it.getLabel())
                                .userId(it.getUser_id())
                                .mediaId(cache.get(it.getData_id()).getMediaId())
                                .build())
                        .collect(Collectors.toList());
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("di ting batch detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectAsyncData asyncDetectImage(List<MediaDetectUrlRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            throw new RuntimeException();
        }

        log.info("async detect image requests: " + requests);
        List<ImageInspectRequestTask> tasks = requests.stream()
                .map(it -> ImageInspectRequestTask.builder()
                        .data_id(it.getDataId())
                        .url(it.getUrl())
                        .user_id(it.getUserId())
                        .build())
                .collect(Collectors.toList());
        Call<InspectAsyncResponse> call = diTingApi.inspectAsyncImage(ImageInspectRequest.builder()
                .token(properties.getToken())
                .scenes(properties.getImageScenes())
                .tasks(tasks)
                .build());
        try {
            //noinspection DuplicatedCode
            Response<InspectAsyncResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                InspectAsyncResponse body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (body.getCode() != 200) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return MediaDetectAsyncData.builder()
                        .taskId(body.getTask_id())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("di ting async detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData queryAsyncDetectImage(String taskId) {
        InspectAsyncResultRequest request = InspectAsyncResultRequest.builder()
                .token(properties.getToken())
                .task_id(taskId)
                .build();
        Call<ImageInspectResultResponse> call = diTingApi.imageAsyncResult(request);
        try {
            Response<ImageInspectResultResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                ImageInspectResultResponse body = execute.body();
                // 210 表示检测中，我们返回 null 记录一下检测次数
                if (body == null || body.getCode() == 210) {
                    return null;
                }
                if (body.getCode() != 200 || body.getResults() == null || body.getResults().isEmpty()) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return body.getResults().stream()
                        .map(it -> MediaDetectData.builder()
                                .suggestion(it.getSuggestion())
                                .dataId(it.getData_id())
                                .label(it.getLabel())
                                .build())
                        .findFirst()
                        .orElse(null);
            }
            // 这里可能不是成功的 http 请求，需要返回 null 表示查询失败，而不是检测失败或者服务异常
            return null;
        } catch (IOException e) {
            log.error("di ting query async detect image failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectAsyncData asyncDetectVideo(MediaDetectUrlRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("url", request.getUrl());
        objectNode.put("data_id", request.getDataId());
        Call<InspectAsyncResponse> call = diTingApi.videoAsyncInspect(VideoInspectRequest.builder()
                .token(properties.getToken())
                .image_scenes(properties.getImageScenes())
                .audio_scenes(properties.getAudioScenes())
                .task(objectNode)
                .build());
        try {
            //noinspection DuplicatedCode
            Response<InspectAsyncResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                InspectAsyncResponse body = execute.body();
                if (body == null) {
                    throw new MediaDetectServerException("no body");
                }
                if (body.getCode() != 200) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return MediaDetectAsyncData.builder()
                        .taskId(body.getTask_id())
                        .build();
            }
            throw new MediaDetectServerException(execute.message());
        } catch (IOException e) {
            log.error("di ting async detect video failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

    @Override
    public MediaDetectData queryAsyncDetectVideo(String taskId) {
        InspectAsyncResultRequest request = InspectAsyncResultRequest.builder()
                .token(properties.getToken())
                .task_id(taskId)
                .build();
        Call<VideoInspectResultResponse> call = diTingApi.videoAsyncResult(request);
        try {
            Response<VideoInspectResultResponse> execute = call.execute();
            if (execute.isSuccessful()) {
                VideoInspectResultResponse body = execute.body();
                // 210 表示检测中，我们返回 null 记录一下检测次数
                if (body == null || body.getCode() == 210) {
                    return null;
                }
                if (body.getCode() != 200) {
                    throw new MediaDetectFailureException(body.getMsg());
                }
                return MediaDetectData.builder()
                        .dataId(body.getData_id())
                        .suggestion(body.getSuggestion())
                        .build();
            }
            // 这里可能不是成功的 http 请求，需要返回 null 表示查询失败，而不是检测失败或者服务异常
            return null;
        } catch (IOException e) {
            log.error("di ting query async detect video failure!", e);
            throw new MediaDetectServerException(e.getMessage());
        }
    }

}
