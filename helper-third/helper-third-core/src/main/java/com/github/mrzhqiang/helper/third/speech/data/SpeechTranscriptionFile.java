package com.github.mrzhqiang.helper.third.speech.data;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批量转录结果的文件数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-results">get-transcription-results</a>
 */
@Data
public class SpeechTranscriptionFile {

    /**
     * 转录文件。
     */
    public static final String KIND_TRANSCRIPTION = "Transcription";
    /**
     * 转录报告。
     */
    public static final String KIND_TRANSCRIPTION_REPORT = "TranscriptionReport";

    private String self;
    private String name;
    /**
     * 文件种类：Transcription or TranscriptionReport。
     */
    private String kind;
    private Properties properties;
    private LocalDateTime createdDateTime;
    private Links links;

    @Data
    public static class Properties {

        private Integer size;

    }

    @Data
    public static class Links {

        private String contentUrl;

    }

}
