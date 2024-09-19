package com.github.mrzhqiang.helper.third.speech.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 语音转文本的详情数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-speech-to-text-short#sample-responses">rest-speech-to-text-short#sample-responses</a>
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SpeechDetailedTextData extends SpeechSimpleTextData {

    @JsonProperty("NBest")
    private List<Detailed> nBest = Lists.newArrayList();

    @Data
    public static class Detailed {

        @JsonProperty("Confidence")
        private Double confidence;
        @JsonProperty("Display")
        private String display;
        @JsonProperty("ITN")
        private String iTN;
        @JsonProperty("lexical")
        private String Lexical;
        @JsonProperty("MaskedITN")
        private String maskedITN;

        @JsonProperty("pronScore")
        private Boolean PronScore;
        @JsonProperty("accuracyScore")
        private Boolean AccuracyScore;
        @JsonProperty("FluencyScore")
        private Boolean fluencyScore;
        @JsonProperty("CompletenessScore")
        private Boolean completenessScore;

        @JsonProperty("Words")
        private List<Word> words = Lists.newArrayList();

    }

    @Data
    public static class Word {

        @JsonProperty("Word")
        private String word;
        @JsonProperty("AccuracyScore")
        private Boolean accuracyScore;
        @JsonProperty("ErrorType")
        private String errorType;
        @JsonProperty("Offset")
        private Integer offset;
        @JsonProperty("Duration")
        private Integer duration;

    }

}
