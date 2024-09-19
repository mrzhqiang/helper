package com.github.mrzhqiang.helper.third.speech.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 语音转文本的简单数据。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-speech-to-text-short#sample-responses">rest-speech-to-text-short#sample-responses</a>
 */
@Data
public class SpeechSimpleTextData {

    /**
     * 状态：Success
     */
    @JsonProperty("RecognitionStatus")
    private String recognitionStatus;
    @JsonProperty("DisplayText")
    private String displayText;
    @JsonProperty("Offset")
    private Integer offset;
    @JsonProperty("Duration")
    private Integer duration;

}
