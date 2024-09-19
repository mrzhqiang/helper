package com.github.mrzhqiang.helper.third.translate;

import lombok.Data;

/**
 * 文档翻译的请求体。
 */
@Data
public class DocumentTranslateBody {

    /**
     * 要翻译的文档文件。
     */
    private Object document;
    /**
     * 要翻译的语言代码。
     */
    private String languageCode = AzureTranslateTextApi.EN;

}
