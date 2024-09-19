package com.github.mrzhqiang.helper.third.speech;

import com.github.mrzhqiang.helper.StringUtils;
import com.github.mrzhqiang.helper.third.core.DownloadApi;
import static com.github.mrzhqiang.helper.third.speech.AzureSpeechSttApi.AUDIO_WAV_MEDIA_TYPE;
import static com.github.mrzhqiang.helper.third.speech.AzureSpeechSttApi.SHORT_AUDIO_QUERY_MAP;
import com.github.mrzhqiang.helper.third.speech.data.SpeechSimpleTextData;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionCreateBody;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionData;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionFile;
import com.github.mrzhqiang.helper.third.speech.data.SpeechTranscriptionResult;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class SpeechServiceImpl implements SpeechService {

    public static final String OPEN_AI_MODEL_WHISPER_1 = "whisper-1";

    private final DownloadApi downloadApi;
    private final OpenAiSpeechApi openAiSpeechApi;
    private final AzureSpeechApi azureSpeechApi;
    private final AzureSpeechSttApi azureSpeechSttApi;
    private final AzureSpeechTtsApi azureSpeechTtsApi;

    private String token;
    private LocalDateTime tokenTime = LocalDateTime.now();

    @Override
    public SpeechTextDto speechToText(SpeechUrlDto dto) {
        Preconditions.checkNotNull(dto, "speech url dto == null");
        Preconditions.checkArgument(StringUtils.hasText(dto.getUrl()), "url must be not empty");

        try {
            String url = dto.getUrl();
            Response<ResponseBody> response = downloadApi.download(url).execute();
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }

            // TODO 建立临时文件的规范，避免硬编码，建立定期清理临时文件的机制
            Path tempPath = Paths.get("./temp", LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
            if (url.contains("/")) {
                tempPath = tempPath.resolve(url.substring(url.lastIndexOf("/")));
            }
            try (ResponseBody responseBody = response.body()) {
                if (responseBody != null) {
                    Files.copy(responseBody.byteStream(), tempPath, StandardCopyOption.REPLACE_EXISTING);
                    File file = tempPath.toFile();
                    MultipartBody requestBody = new MultipartBody.Builder()
                            .setType(MediaType.get("multipart/form-data"))
                            .addFormDataPart("model", OPEN_AI_MODEL_WHISPER_1)
                            .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.get("*/*"), file))
                            .build();
                    Response<SpeechTextDto> dtoResponse = openAiSpeechApi.audioTranscriptions(requestBody).execute();
                    if (dtoResponse.isSuccessful()) {
                        return dtoResponse.body();
                    }
                }
            }
        } catch (IOException e) {
            log.error("api speech to text is failure.", e);
        }
        throw new RuntimeException();
    }

    @Override
    public SpeechTextDto speechToText(byte[] files) {
        Preconditions.checkNotNull(files, "speech file == null");
        Preconditions.checkArgument(files.length > 0, "speech file must be not empty");

        try {
            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MediaType.get("multipart/form-data"))
                    .addFormDataPart("model", OPEN_AI_MODEL_WHISPER_1)
                    .addFormDataPart("file", System.currentTimeMillis() + ".mp3", RequestBody.create(MediaType.get("*/*"), files))
                    .build();
            Response<SpeechTextDto> dtoResponse = openAiSpeechApi.audioTranscriptions(requestBody).execute();
            if (dtoResponse.isSuccessful()) {
                return dtoResponse.body();
            }
        } catch (IOException e) {
            log.error("api speech to text is failure.", e);
        }
        throw new RuntimeException();
    }

    @Override
    public SpeechTextDto transcription(SpeechUrlDto dto) {
        Preconditions.checkNotNull(dto, "speech url dto == null");
        Preconditions.checkArgument(StringUtils.hasText(dto.getUrl()), "url must be not empty");

        try {
            Response<SpeechTranscriptionData> dataResponse = azureSpeechApi.createTranscription(SpeechTranscriptionCreateBody.of(dto.getUrl())).execute();
            if (dataResponse.isSuccessful()) {
                SpeechTranscriptionData data = dataResponse.body();
                if (data != null) {
                    // 重复发起 10 次请求，每次间隔 2 秒，总共约 20 秒
                    return azureSpeechApi.getTranscription(data.getSelf())
                            .repeat(10)
                            .delay(2, TimeUnit.SECONDS)
                            .filter(SpeechTranscriptionData::isSucceeded)
                            .firstElement()
                            .flatMap(it -> azureSpeechApi.getTranscriptionFileList(it.getLinks().getFiles()))
                            .map(it -> it.getValues().stream()
                                    .map(SpeechTranscriptionFile::getSelf)
                                    .findFirst()
                                    .orElse(""))
                            .filter(StringUtils::hasText)
                            .flatMap(azureSpeechApi::getTranscriptionsResult)
                            .map(it -> it.getCombinedRecognizedPhrases().stream()
                                    .map(SpeechTranscriptionResult.CombinedRecognizedPhrase::getDisplay)
                                    .findFirst()
                                    .orElse(""))
                            .filter(StringUtils::hasText)
                            .map(SpeechTextDto::of)
                            .blockingGet();
                }
            }
        } catch (IOException e) {
            log.error("api speech transcription is failure.", e);
        }
        throw new RuntimeException();
    }

    @Override
    public SpeechTextDto stt(SpeechUrlDto dto) {
        Preconditions.checkNotNull(dto, "speech url dto == null");
        Preconditions.checkArgument(StringUtils.hasText(dto.getUrl()), "url must be not empty");

        try {
            Response<ResponseBody> execute = downloadApi.download(dto.getUrl()).execute();
            if (execute.isSuccessful()) {
                try (ResponseBody responseBody = execute.body()) {
                    if (responseBody != null) {
                        /*
                         * The access token should be sent to the service as the header.
                         * Each access token is valid for 10 minutes.
                         * You can get a new token at any time, but to minimize network traffic and latency,
                         * we recommend using the same token for nine minutes.Authorization: Bearer <TOKEN>
                         *
                         * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/speech-service/rest-speech-to-text-short#how-to-use-an-access-token">how-to-use-an-access-token</a>
                         */
                        if (!StringUtils.hasText(token) || LocalDateTime.now().isAfter(tokenTime.plusMinutes(9))) {
                            token = "Bearer " + azureSpeechApi.issueToken().blockingGet();
                            tokenTime = LocalDateTime.now();
                        }
                        RequestBody body = RequestBody.create(AUDIO_WAV_MEDIA_TYPE, responseBody.bytes());
                        return azureSpeechSttApi.simpleStt(token, SHORT_AUDIO_QUERY_MAP, body)
                                .map(SpeechSimpleTextData::getDisplayText)
                                .map(SpeechTextDto::of)
                                .blockingGet();
                    }
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public SpeechUrlDto tts(SpeechTextDto dto) {
        Preconditions.checkNotNull(dto, "speech text dto == null");
        String text = dto.getText();
        Preconditions.checkArgument(StringUtils.hasText(text), "text must be not empty");

        if (!StringUtils.hasText(token) || LocalDateTime.now().isAfter(tokenTime.plusMinutes(9))) {
            token = "Bearer " + azureSpeechApi.issueToken().blockingGet();
            tokenTime = LocalDateTime.now();
        }
        return azureSpeechTtsApi.tts(token, dto.getText())
                // FIXME 需要将响应流上传到某个地方，再提供 url 到响应内容
                .map(it -> new SpeechUrlDto())
                .blockingGet();
    }

}
