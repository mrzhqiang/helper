package com.github.mrzhqiang.helper.third.speech;

import io.reactivex.Maybe;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Azure 文本转语音接口。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-text-to-speech?tabs=streaming">rest-text-to-speech</a>
 */
public interface AzureSpeechTtsApi {

    String USER_AGENT = "Phiz Text To Speech";

    /**
     * 文本转语音接口。
     * <p>
     * <a href="https://<REGION_IDENTIFIER>.tts.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1">Azure Tts Api</a>
     * <p>
     * {@code <speak version='1.0' xml:lang='en-US'>
     * <voice xml:lang='en-US' xml:gender='Male' name='en-US-ChristopherNeural'>
     * I'm excited to try text to speech!
     * </voice></speak>
     * }
     *
     * @param token   授权令牌。
     * @param content 发送内容。FIXME 目前没有调用这个接口，所以直接使用字符串作为请求体，后续如果启用，再引入 xml 序列化。
     * @return 可能的反应结果，只有单个、无或者失败回调。
     */
    @Headers({
            "Content-Type: application/ssml+xml",
            "X-Microsoft-OutputFormat: riff-24khz-16bit-mono-pcm",
            "User-Agent: " + USER_AGENT,
    })
    @POST("/speech/recognition/conversation/cognitiveservices/v1")
    Maybe<ResponseBody> tts(@Header("Authorization") String token, @Body String content);

}
