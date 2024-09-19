package com.github.mrzhqiang.helper.third.translate;

import com.github.mrzhqiang.helper.third.translate.data.TranslateContent;
import io.reactivex.Maybe;
import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

/**
 * Azure 文档翻译接口。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/translator/document-translation/overview">document-translation</a>
 */
public interface AzureTranslateDocumentApi {

    String TARGET_LANGUAGE = "pt";

    String API_VERSION = "2023-11-01-preview";

    @POST("/translator/document:translate")
    Maybe<List<TranslateContent>> translate(@Query("targetLanguage") String targetLanguage,
                                            @Query("api-version") String apiVersion,
                                            @Body MultipartBody document);

}
