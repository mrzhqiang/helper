package com.github.mrzhqiang.helper.third.translate;

import lombok.Data;

/**
 * 文本翻译的请求体。
 */
@Data
public class TextTranslateBody {

    /**
     * 要翻译的文本内容。
     */
    private String text;
    /**
     * 要翻译的语言代码。
     */
    private String languageCode = AzureTranslateTextApi.EN;

}
