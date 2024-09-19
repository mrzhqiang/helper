package com.github.mrzhqiang.helper.third.translate;

/**
 * 翻译服务。
 */
public interface TranslateService {

    String LANG_CODE_AZURE_JSON = "lang_code_azure.json";

    /**
     * 翻译文本。
     *
     * @param body 文本翻译请求体。
     * @return 翻译的文本数据。
     */
    TranslateTextData translate(TextTranslateBody body);

}
