package com.github.mrzhqiang.helper.third.translate;

import lombok.Data;

/**
 * 文本翻译的响应数据。
 */
@Data
public class TranslateTextData {

    /**
     * 已翻译的文本内容。
     */
    private String text;
    /**
     * 已翻译的语言代码。
     */
    private String languageCode;

}
