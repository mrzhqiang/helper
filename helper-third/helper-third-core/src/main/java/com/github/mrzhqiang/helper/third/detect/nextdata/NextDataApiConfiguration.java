package com.github.mrzhqiang.helper.third.detect.nextdata;

import lombok.RequiredArgsConstructor;
import retrofit2.Retrofit;

@RequiredArgsConstructor
public class NextDataApiConfiguration {

    private final NextDataApiProperties properties;

    public NextDataTextApi nextDataTextApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getTextHost())
                .build().create(NextDataTextApi.class);
    }

    public NextDataImageApi nextDataImageApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getImageHost())
                .build().create(NextDataImageApi.class);
    }

    public NextDataAsyncImageApi nextDataAsyncImageApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getAsyncImageHost())
                .build().create(NextDataAsyncImageApi.class);
    }

    public NextDataQueryApi nextDataQueryApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getAsyncImageQueryHost())
                .build().create(NextDataQueryApi.class);
    }

    public NextDataVideoApi nextDataVideoApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(properties.getVideoHost())
                .build().create(NextDataVideoApi.class);
    }

}
