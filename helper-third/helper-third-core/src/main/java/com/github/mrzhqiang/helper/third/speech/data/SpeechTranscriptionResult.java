package com.github.mrzhqiang.helper.third.speech.data;

import com.google.common.collect.Lists;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 批量转录文件数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-get?pivots=rest-api#get-transcription-results">get-transcription-results</a>
 */
@Data
public class SpeechTranscriptionResult {

    private String source;
    private LocalDateTime timestamp;
    private Long durationInTicks;
    private Duration duration;
    private List<CombinedRecognizedPhrase> combinedRecognizedPhrases = Lists.newArrayList();
    private List<RecognizedPhrase> recognizedPhrases = Lists.newArrayList();

    @Data
    public static class CombinedRecognizedPhrase {

        private Integer channel;
        private String lexical;
        private String itn;
        private String maskedITN;
        private String display;

    }

    @Data
    public static class RecognizedPhrase {

        private String recognitionStatus;
        private Integer channel;
        private Duration offset;
        private Duration duration;
        private Double offsetInTicks;
        private Double durationInTicks;
        private List<BestRecognizedPhrase> nBest = Lists.newArrayList();

    }

    @Data
    public static class BestRecognizedPhrase {

        private Double confidence;
        private String lexical;
        private String itn;
        private String maskedITN;
        private String display;
        private List<DisplayWord> displayWords = Lists.newArrayList();

    }

    @Data
    public static class DisplayWord {

        private String displayText;
        private Duration offset;
        private Duration duration;
        private Double offsetInTicks;
        private Double durationInTicks;

    }

}
