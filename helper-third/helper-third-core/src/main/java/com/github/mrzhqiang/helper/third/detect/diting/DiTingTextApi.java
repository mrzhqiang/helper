package com.github.mrzhqiang.helper.third.detect.diting;

import com.github.mrzhqiang.helper.third.detect.diting.dto.TextInspectRequest;
import com.github.mrzhqiang.helper.third.detect.diting.dto.TextInspectResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface DiTingTextApi {

    /**
     * 检测文本
     */
    @POST("/v4.0/game_chat_ban/detect_text")
    Call<TextInspectResponse> detectText(@Body TextInspectRequest request);

}
