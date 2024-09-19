package com.github.mrzhqiang.helper.third.speech.data;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 批量转录结果的报告数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-results">get-transcription-results</a>
 */
@Data
public class SpeechTranscriptionReport {

    private Integer successfulTranscriptionsCount;
    private Integer failedTranscriptionsCount;
    private List<Detail> details = Lists.newArrayList();

    public static class Detail {

        private String source;
        private String status;

    }

}
