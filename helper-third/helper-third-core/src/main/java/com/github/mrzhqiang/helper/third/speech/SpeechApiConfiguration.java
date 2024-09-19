package com.github.mrzhqiang.helper.third.speech;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mrzhqiang.helper.CollectionUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Map;

@RequiredArgsConstructor
public class SpeechApiConfiguration {

    private final AzureSpeechApiProperties azureSpeechApiProperties;
    private final OpenAiSpeechApiProperties openAiSpeechApiProperties;

    public Retrofit azureRetrofit(OkHttpClient okHttpClient, Retrofit retrofit) {
        OkHttpClient httpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    Map<String, String> headers = azureSpeechApiProperties.getHeaders();
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(requestBuilder::addHeader);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        return retrofit.newBuilder()
                .baseUrl(azureSpeechApiProperties.getHost())
                .client(httpClient)
                .build();
    }

    public AzureSpeechApi azureSpeechApi(Retrofit azureRetrofit) {
        return azureRetrofit.create(AzureSpeechApi.class);
    }

    public AzureSpeechSttApi azureSpeechSttApi(OkHttpClient okHttpClient, Retrofit retrofit) {
        OkHttpClient httpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    Map<String, String> headers = azureSpeechApiProperties.getHeaders();
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(requestBuilder::addHeader);
                    }
                    requestBuilder.addHeader("Content-type", AzureSpeechSttApi.STT_CONTENT_TYPE);
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        ObjectMapper mapper = new ObjectMapper();
        return retrofit.newBuilder()
                .baseUrl(azureSpeechApiProperties.getSttHost())
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build().create(AzureSpeechSttApi.class);
    }

    public AzureSpeechTtsApi azureSpeechTtsApi(Retrofit azureRetrofit) {
        return azureRetrofit.newBuilder()
                .baseUrl(azureSpeechApiProperties.getTtsHost())
                .build().create(AzureSpeechTtsApi.class);
    }

    public OpenAiSpeechApi openAiSpeechApi(OkHttpClient proxyOkHttpClient, Retrofit retrofit) {
        OkHttpClient httpClient = proxyOkHttpClient.newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    Map<String, String> headers = openAiSpeechApiProperties.getHeaders();
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(requestBuilder::addHeader);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        return retrofit.newBuilder()
                .baseUrl(openAiSpeechApiProperties.getHost())
                .client(httpClient)
                .build().create(OpenAiSpeechApi.class);
    }

}
