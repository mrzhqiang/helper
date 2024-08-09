package com.github.mrzhqiang.helper.third.detect.diting;

import lombok.RequiredArgsConstructor;
import retrofit2.Retrofit;

/**
 * DiTing 接口配置。
 *
 * @see <a href="https://gtf.ai.xingzheai.cn">国内（文本正在使用——阿里云上海服务器）</a>
 * @see <a href="https://gtf.ai-abroad.xingzheai.cn">海外（无法使用——阿里云香港服务器）</a>
 * @see <a href="https://diting.xingzheai.cn">国内（图片视频正在使用——阿里云上海服务器）</a>
 */
@RequiredArgsConstructor
public class DiTingApiConfiguration {

    private final DiTingApiProperties properties;

    public DiTingApi diTingApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getHost())
                .build().create(DiTingApi.class);
    }

    public DiTingTextApi diTingTextApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getTextHost())
                .build().create(DiTingTextApi.class);
    }

}
