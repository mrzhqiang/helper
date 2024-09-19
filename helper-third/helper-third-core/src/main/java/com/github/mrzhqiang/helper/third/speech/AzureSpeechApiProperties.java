package com.github.mrzhqiang.helper.third.speech;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

/**
 * Azure 的语音属性。
 */
@Getter
@Setter
public class AzureSpeechApiProperties {

    private static final String DEF_HOST = "https://westus.api.cognitive.microsoft.com";
    /**
     * Speech To Text
     */
    private static final String DEF_STT_HOST = "https://westus.stt.speech.microsoft.com";
    /**
     * Text To Speech
     */
    private static final String DEF_TTS_HOST = "https://westus.tts.speech.microsoft.com";

    /**
     * 主机地址。
     */
    private String host = DEF_HOST;
    /**
     * 语音转文本。
     */
    private String sttHost = DEF_STT_HOST;
    /**
     * 文本转语音。
     */
    private String ttsHost = DEF_TTS_HOST;
    /**
     * 请求头数据。
     * <p>
     * 比如：
     * <pre>
     * headers:
     *   "Authorization": "Basic XXX"
     *   "[Authorization]": "Bearer XXX"
     * </pre>
     * 注意：未使用 [] 的情况下, 非字母数字、- 符号或者 . 符号会被移除。
     */
    private Map<String, String> headers = Collections.emptyMap();

}
