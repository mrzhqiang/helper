package com.github.mrzhqiang.helper.third.translate.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TranslateTextCreateBody {

    public static TranslateTextCreateBody of(String text) {
        TranslateTextCreateBody body = new TranslateTextCreateBody();
        body.text = text;
        return body;
    }

    @JsonProperty("Text")
    private String text;

}
