package com.github.mrzhqiang.helper.third.speech;

import lombok.Data;

@Data
public class SpeechTextDto {

    private String text;

    public static SpeechTextDto of(String display) {
        SpeechTextDto dto = new SpeechTextDto();
        dto.text = display;
        return dto;
    }

}
