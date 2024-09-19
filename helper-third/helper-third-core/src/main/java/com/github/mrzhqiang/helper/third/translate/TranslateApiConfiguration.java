package com.github.mrzhqiang.helper.third.translate;

import com.github.mrzhqiang.helper.CollectionUtils;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

import java.util.Map;

@RequiredArgsConstructor
public class TranslateApiConfiguration {

    private final AzureTranslateApiProperties properties;

    public Retrofit azureTranslateRetrofit(OkHttpClient okHttpClient, Retrofit retrofit) {
        OkHttpClient httpClient = okHttpClient.newBuilder()
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    Map<String, String> headers = properties.getHeaders();
                    if (!CollectionUtils.isEmpty(headers)) {
                        headers.forEach(requestBuilder::addHeader);
                    }
                    return chain.proceed(requestBuilder.build());
                })
                .build();

        return retrofit.newBuilder()
                .baseUrl(properties.getHost())
                .client(httpClient)
                .build();
    }

    public AzureTranslateTextApi azureTranslateApi(Retrofit azureTranslateRetrofit) {
        return azureTranslateRetrofit.create(AzureTranslateTextApi.class);
    }

    public AzureTranslateAuthApi azureTranslateAuthApi(Retrofit azureTranslateRetrofit) {
        return azureTranslateRetrofit.newBuilder()
                .baseUrl(properties.getAuthHost())
                .build()
                .create(AzureTranslateAuthApi.class);
    }

    public AzureTranslateDocumentApi azureTranslateDocumentApi(Retrofit azureTranslateRetrofit) {
        return azureTranslateRetrofit.newBuilder()
                .baseUrl(properties.getDocumentHost())
                .build()
                .create(AzureTranslateDocumentApi.class);
    }

}
