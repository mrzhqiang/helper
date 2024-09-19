package com.github.mrzhqiang.helper.third.translate.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 翻译内容。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/translator/reference/v3-0-translate#response-body">v3-0-translate#response-body</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TranslateContent {

    private DetectedLanguage detectedLanguage;
    private List<Translation> translations = Lists.newArrayList();

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class DetectedLanguage {

        private String language;
        private Double score;

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Translation {

        private String to;
        private String text;

    }

}
