package com.github.mrzhqiang.helper.third.speech;

import lombok.experimental.UtilityClass;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * 基于 OpenAi 语音转文本的示例。
 *
 * @see <a href="https://platform.openai.com/docs/guides/speech-to-text/overview">Speech to text</a>
 */
@UtilityClass
public class OpenAiSpeechExample {

    private static final String OPEN_AI_AUDIO_TRANSCRIPTIONS_URL = "https://api.openai.com/v1/audio/transcriptions";
    private static final String TOKEN_KEY = "Bearer ";
    private static final String AUDIO_WAV_FILE = "classpath:whatstheweatherlike.wav";
    private static final Logger LOGGER = LoggerFactory.getLogger("okhttp");

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient.Builder()
            .addInterceptor(chain -> chain.proceed(chain.request().newBuilder()
                    .addHeader("Authorization", TOKEN_KEY)
                    .build()))
            .addInterceptor(getLoggingInterceptor())
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.15.217", 1080)))
            .build();

    private static HttpLoggingInterceptor getLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOGGER::info);
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return interceptor;
    }

    public static void main(String[] args) throws IOException {
        speechToText();
    }

    private static void speechToText() throws IOException {
        File audioWavFile = new File(OpenAiSpeechApi.class.getResource(AUDIO_WAV_FILE).getFile());
        RequestBody audioWavBody = RequestBody.create(MediaType.get("audio/wav"), audioWavFile);
        MultipartBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("model", "whisper-1")
                .addFormDataPart("file", audioWavFile.getName(), audioWavBody)
                .build();
        Request request = new Request.Builder()
                .url(OPEN_AI_AUDIO_TRANSCRIPTIONS_URL)
                .post(formBody)
                .build();
        try (Response response = HTTP_CLIENT.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                LOGGER.info(response.body().string());
            } else {
                LOGGER.error("Call failure: " + response.message());
            }
        }
    }

}
