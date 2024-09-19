package com.github.mrzhqiang.helper.third.detect.nextdata;

import lombok.Getter;
import lombok.Setter;

/**
 * Next Data 接口配置。
 *
 * @see <a href="https://nextdata.ai/help/text#Request%20parameters">nextdata.ai</a>
 */
@Getter
@Setter
public class NextDataApiProperties {

    private static final String DEF_TEXT_HOST = "http://api-text-fjny.fengkongcloud.com";
    private static final String DEF_IMAGE_HOST = "http://api-img-gg.fengkongcloud.com";
    private static final String DEF_ASYNC_IMAGE_HOST = "http://api-img-sh.fengkongcloud.com";
    private static final String DEF_ASYNC_IMAGE_QUERY_HOST = "http://api-img-active-query.fengkongcloud.com";
    private static final String DEF_VIDEO_HOST = "http://api-video-gg.fengkongcloud.com";
    private static final String DEF_ACCESS_KEY = "";

    private String textHost = DEF_TEXT_HOST;
    private String imageHost = DEF_IMAGE_HOST;
    private String asyncImageHost = DEF_ASYNC_IMAGE_HOST;
    private String asyncImageQueryHost = DEF_ASYNC_IMAGE_QUERY_HOST;
    private String videoHost = DEF_VIDEO_HOST;
    private String accessKey = DEF_ACCESS_KEY;

}
