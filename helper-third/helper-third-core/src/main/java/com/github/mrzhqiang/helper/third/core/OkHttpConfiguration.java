package com.github.mrzhqiang.helper.third.core;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.io.File;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * Okhttp 配置。
 * <p>
 * 主要定义 OkhttpClient 的参数，并作为 Bean 提供给 Spring 容器。
 */
public final class OkHttpConfiguration {

    public static OkHttpClient okHttpClient(OkHttpProperties properties) {
        return new OkHttpClient.Builder()
                .callTimeout(properties.getCallTimeout())
                .cache(localCache(properties))
                .followSslRedirects(false)
                .followRedirects(false)
                .addInterceptor(loggingInterceptor(properties))
                .build();
    }

    private static Cache localCache(OkHttpProperties properties) {
        File directory = Paths.get(properties.getCachePath()).toFile();
        long maxSize = properties.getCacheMaxSize();
        return new Cache(directory, maxSize);
    }

    private static Interceptor loggingInterceptor(OkHttpProperties properties) {
        Logger logger = Logger.getLogger("okhttp3");
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(logger::info);
        interceptor.setLevel(properties.getLoggingLevel());
        return interceptor;
    }
}
