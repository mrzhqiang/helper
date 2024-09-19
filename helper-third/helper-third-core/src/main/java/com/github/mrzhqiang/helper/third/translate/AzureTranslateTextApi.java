package com.github.mrzhqiang.helper.third.translate;

import com.github.mrzhqiang.helper.third.translate.data.TranslateContent;
import com.github.mrzhqiang.helper.third.translate.data.TranslateTextCreateBody;
import io.reactivex.Maybe;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

/**
 * Azure 文本翻译接口。
 *
 * @see <a href="https://learn.microsoft.com/en-us/azure/ai-services/translator/reference/v3-0-translate">v3-0-translate</a>
 */
public interface AzureTranslateTextApi {

    String API_3_0 = "3.0";
    /**
     * 葡萄牙语。
     */
    String PT = "pt";
    String ZH = "zh-cn";
    String AZURE_ZH = "zh-Hans";
    /**
     * 英语。
     */
    String EN = "en";

    @Headers({"Content-Type: application/json; charset=UTF-8"})
    @POST("/translate")
    Maybe<List<TranslateContent>> translate(@Header("Authorization") String token,
                                            @Body List<TranslateTextCreateBody> bodyList,
                                            @Query("api-version") String apiVersion,
                                            @Query("to") String... to);

}
