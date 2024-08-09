package com.github.mrzhqiang.helper.third.detect.diting;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * DiTing 配置。
 */
@Getter
@Setter
public class DiTingApiProperties {

    private static final String DEF_HOST = "https://diting.xingzheai.cn";
    private static final String DEF_TEXT_HOST = "https://gtf.ai.xingzheai.cn";
    private static final String DEF_TOKEN = "";
    private static final ImmutableList<String> IMAGE_SCENES = ImmutableList.of(
            "porn", "terror", "forbidden"
    );
    private static final ImmutableList<String> AUDIO_SCENES = ImmutableList.of(
            "porn", "terror"
    );

    private String host = DEF_HOST;
    private String textHost = DEF_TEXT_HOST;
    private String token = DEF_TOKEN;
    private List<String> imageScenes = IMAGE_SCENES;
    private List<String> audioScenes = AUDIO_SCENES;


}
