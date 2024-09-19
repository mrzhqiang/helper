package com.github.mrzhqiang.helper.third.speech;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

/**
 * Open Ai 的语音属性。
 */
@Getter
@Setter
public class OpenAiSpeechApiProperties {

    private static final String DEF_HOST = "https://api.openai.com";

    /**
     * 主机地址。
     */
    private String host = DEF_HOST;
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
