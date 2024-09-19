package com.github.mrzhqiang.helper.third.speech.data;

import com.github.mrzhqiang.helper.third.speech.AzureSpeechApi;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * 批量转录的创建请求体。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/batch-transcription-create?pivots=rest-api">batch-transcription-create</a>
 */
@Data
public class SpeechTranscriptionCreateBody {

    /**
     * 公共链接。
     * <p>
     * 必填参数。与 contentContainerUrl 二选一。
     */
    private List<String> contentUrls;
    /**
     * 音频转换预期区域。
     * <p>
     * 必填参数。
     */
    private String locale;
    /**
     * 转录名称。
     * <p>
     * 必填参数。
     * <p>
     * 不唯一，支持更改。
     */
    private String displayName;
    /**
     * 可选参数。
     */
    private String model;
    /**
     * 可选参数。
     */
    private Properties properties;

    public static SpeechTranscriptionCreateBody of(String url) {
        SpeechTranscriptionCreateBody body = new SpeechTranscriptionCreateBody();
        body.setContentUrls(ImmutableList.of(url));
        body.setLocale(AzureSpeechApi.LOCALE_PT_BR);
        body.setDisplayName(AzureSpeechApi.DISPLAY_NAME);
        Properties properties = new Properties();
        properties.setWordLevelTimestampsEnabled(false);
        LanguageIdentification languageIdentification = new LanguageIdentification();
        languageIdentification.setCandidateLocales(ImmutableList.of(AzureSpeechApi.LOCALE_PT_BR, AzureSpeechApi.LOCALE_EN_US));
        properties.setLanguageIdentification(languageIdentification);
        body.setProperties(properties);
        return body;
    }

    @Data
    public static class Properties {

        private boolean wordLevelTimestampsEnabled;
        private LanguageIdentification languageIdentification;

    }

    @Data
    public static class LanguageIdentification {

        private List<String> candidateLocales = Lists.newArrayList();

    }

}
