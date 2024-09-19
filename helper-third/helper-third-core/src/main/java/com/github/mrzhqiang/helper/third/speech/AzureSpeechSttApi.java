package com.github.mrzhqiang.helper.third.speech;

import com.github.mrzhqiang.helper.third.speech.data.SpeechSimpleTextData;
import com.google.common.collect.ImmutableMap;
import io.reactivex.Maybe;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

import java.util.Map;

/**
 * Azure 短音频转文字接口。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-speech-to-text">rest-speech-to-text</a>
 */
public interface AzureSpeechSttApi {

    MediaType AUDIO_WAV_MEDIA_TYPE = MediaType.get("audio/*");

    String STT_CONTENT_TYPE = "audio/wav; codecs=audio/pcm; samplerate=16000";

    Map<String, String> SHORT_AUDIO_QUERY_MAP = ImmutableMap.of(
            // https://learn.microsoft.com/en-us/azure/ai-services/speech-service/language-support?tabs=stt
            "language", AzureSpeechApi.LOCALE_PT_BR,
            // simple or detailed.
            // Simple results include RecognitionStatus, DisplayText, Offset, and Duration.
            // Detailed responses include four different representations of display text.
            "format", "simple",
            // Specifies how to handle profanity in recognition results. Accepted values are:
            // masked, which replaces profanity with asterisks.
            // removed, which removes all profanity from the result.
            // raw, which includes profanity in the result.
            // The default setting is masked.
            "profanity", "masked"
    );

    /**
     * 语音转文本接口。
     * <p>
     * <a href="https://<REGION_IDENTIFIER>.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1">Azure Stt Api</a>
     * <p>
     * 主要是短音频，不超过 60s 的时间长度。
     * <p>
     * 返回简单的文本内容，不包含文本详情——分段内容。
     *
     * @param token    授权令牌。
     * @param queryMap 查询参数。
     * @param audio    音频文件。
     * @return 可能的反应结果，只有单个、无或者失败回调。
     */
    @POST("/speech/recognition/conversation/cognitiveservices/v1")
    Maybe<SpeechSimpleTextData> simpleStt(@Header("Authorization") String token,
                                          @QueryMap Map<String, String> queryMap,
                                          @Body RequestBody audio);

}
