package com.github.mrzhqiang.helper.third.translate;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Azure 的翻译属性。
 */
@Getter
@Setter
public class AzureTranslateApiProperties {

    private static final String DEF_TEXT_HOST = "https://api.cognitive.microsofttranslator.com";
    private static final String DEF_AUTH_HOST = "https://westus.api.cognitive.microsoft.com";
    private static final String DEF_DOCUMENT_HOST = "https://phiz-translator.cognitiveservices.azure.com";

    private String host = DEF_TEXT_HOST;
    private String authHost = DEF_AUTH_HOST;
    private String documentHost = DEF_DOCUMENT_HOST;
    private Map<String, String> headers = Maps.newHashMap();

}
