package com.github.mrzhqiang.helper.third.speech;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Open Ai 语音接口。
 */
public interface OpenAiSpeechApi {

    /**
     * 音频转换。
     *
     * @param body 请求。
     * @return 包含文本内容的数据传输对象。
     */
    @POST("/v1/audio/transcriptions")
    Call<SpeechTextDto> audioTranscriptions(@Body MultipartBody body);

}
