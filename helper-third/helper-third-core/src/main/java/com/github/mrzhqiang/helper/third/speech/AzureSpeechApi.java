package com.github.mrzhqiang.helper.third.speech;

import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionCreateBody;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionData;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionFile;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionFileList;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionResult;
import io.reactivex.Maybe;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Azure 语音接口。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-speech-to-text">rest-speech-to-text</a>
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-text-to-speech?tabs=streaming">rest-text-to-speech</a>
 */
public interface AzureSpeechApi {

    String DISPLAY_NAME = "Phiz Speech To Text";
    /**
     * 英语-美国。
     */
    String LOCALE_EN_US = "en-US";
    /**
     * 葡萄牙语-巴西
     */
    String LOCALE_PT_BR = "pt-BR";

    /**
     * 创建批量转录。
     * <p>
     * 对于批量转录，相当于提交任务到一个队列，然后间隔一定时间去获取任务状态，等到任务完成，再获取最终结果。
     *
     * @return 请求调用。
     */
    @POST("/speechtotext/v3.1/transcriptions")
    Call<SpeechTranscriptionData> createTranscription(@Body SpeechTranscriptionCreateBody body);

    /**
     * 获取批量转录。
     * <p>
     * 批量转录状态为 Running 表示执行中，状态为 Succeeded 为
     *
     * @param self 获取状态的链接。通常是创建时返回的 {@link SpeechTranscriptionData#getSelf()} 字符串。
     * @return 可能的反应结果，只有单个、无或者失败回调。
     */
    @GET
    Maybe<SpeechTranscriptionData> getTranscription(@Url String self);

    /**
     * 获取批量转录文件列表。
     *
     * @param files 批量转录文件的链接。通常是批量转录数据的 {@link SpeechTranscriptionData#getLinks()} 中的 files 字符串。
     * @return 可能的反应结果，只有单个、无或者失败回调。
     */
    @GET
    Maybe<SpeechTranscriptionFileList> getTranscriptionFileList(@Url String files);

    /**
     * 获取批量转录结果。
     *
     * @param self 结果文件的链接。通常是批量转录文件的 {@link SpeechTranscriptionFile#getSelf()} 结果链接。
     * @return 可能的反应结果，只有单个、无或者失败回调。
     */
    @GET
    Maybe<SpeechTranscriptionResult> getTranscriptionsResult(@Url String self);

    /**
     * 获取 Token 用于认证。
     *
     * @return 单个反应结果，只有成功或者失败回调。
     */
    @POST("/sts/v1.0/issueToken")
    Single<String> issueToken();

}
