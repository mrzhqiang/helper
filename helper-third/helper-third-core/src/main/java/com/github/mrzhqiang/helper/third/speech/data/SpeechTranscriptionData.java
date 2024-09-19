package com.github.mrzhqiang.helper.third.speech.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批量转录创建后的响应数据。
 * <p>
 * 同时也是获取批量转录状态的响应数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-create?pivots=rest-api">batch-transcription-create</a>
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-status">batch-transcription-get</a>
 */
@Data
public class SpeechTranscriptionData {

    public static final String NOT_STARTED = "NotStarted";
    public static final String RUNNING = "Running";
    public static final String SUCCEEDED = "Succeeded";

    /**
     * The top-level self property in the response body is the transcription's URI.
     * <p>
     * Use this URI to <a href="https://eastus.dev.cognitive.microsoft.com/docs/services/speech-to-text-api-v3-1/operations/Transcriptions_Get">get</a> details
     * such as the URI of the transcriptions and transcription report files.
     * <p>
     * You also use this URI to <a href="https://eastus.dev.cognitive.microsoft.com/docs/services/speech-to-text-api-v3-1/operations/Transcriptions_Update">update</a>
     * or <a href="https://eastus.dev.cognitive.microsoft.com/docs/services/speech-to-text-api-v3-1/operations/Transcriptions_Delete">delete</a> a transcription.
     */
    private String self;
    private Links links;
    private LocalDateTime lastActionDateTime;
    /**
     * 任务状态。
     * <p>
     * 默认情况下是 NotStarted，运行中是 Running，运行成功是 Succeeded。
     */
    private String status;
    private LocalDateTime createdDateTime;
    private String locale;
    private String displayName;

    /**
     * 判断任务是否运行成功。
     *
     * @return 返回 True 表示运行成功；返回 False 表示未开始或运行中，也可能是运行失败。
     */
    public boolean isSucceeded() {
        return SUCCEEDED.equals(status);
    }

    @Data
    public static class Links {

        /**
         * 文件链接。
         * <p>
         * 通过文件链接，可以获取语音文件进行批量转录后的文本内容。
         *
         * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-results">get-transcription-results</a>
         */
        private String files;

    }

}
