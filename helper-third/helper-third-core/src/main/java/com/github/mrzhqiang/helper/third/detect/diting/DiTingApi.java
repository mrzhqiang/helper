package com.github.mrzhqiang.helper.third.detect.diting;

import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.ImageInspectResultResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.InspectAsyncResponse;
import com.github.mrzhqiang.helper.third.detect.diting.dto.InspectAsyncResultRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.VideoInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.VideoInspectResultResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 谛听 API 接口。
 */
public interface DiTingApi {

    /**
     * 检测图片 同步
     *
     * @param request
     * @return
     */
    @POST("/api/v2.0/image/inspect")
    Call<ImageInspectResponse> inspectImage(@Body ImageInspectRequest request);

    /**
     * 检测图片 异步
     *
     * @param request
     * @return
     */
    @POST("/api/v2.0/image/async/inspect")
    Call<InspectAsyncResponse> inspectAsyncImage(@Body ImageInspectRequest request);

    /**
     * 图片异步检测-查询任务结果接⼝
     *
     * @param request
     * @return
     */
    @POST("/api/v2.0/image/async/result")
    Call<ImageInspectResultResponse> imageAsyncResult(@Body InspectAsyncResultRequest request);


    /**
     * 视频异步检测-任务提交接⼝
     *
     * @param request
     * @return
     */
    @POST("/api/v2.0/video/async/inspect")
    Call<InspectAsyncResponse> videoAsyncInspect(@Body VideoInspectRequest request);

    /**
     * 视频异步检测-查询任务结果接⼝
     *
     * @param request
     * @return
     */
    @POST("/api/v2.0/video/async/result")
    Call<VideoInspectResultResponse> videoAsyncResult(@Body InspectAsyncResultRequest request);

}
