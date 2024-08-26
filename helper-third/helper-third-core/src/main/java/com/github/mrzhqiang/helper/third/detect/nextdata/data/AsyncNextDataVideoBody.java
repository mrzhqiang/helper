package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异步的 Next Data 视频请求体。
 *
 * @see <a href="https://nextdata.ai/help/video#Query%20Video%20Results">Query Video Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsyncNextDataVideoBody {

    public static final String DEF_ACCEPT_LANG = "en";

    /**
     * Used for permission authentication, provided by NextData when the account service is opened
     */
    private String accessKey;
    /**
     * Video unique identifier, used to query recognition results, up to 64 characters
     */
    private String btId;
    /**
     * Optional language type of returned information:
     * <pre>
     * zh: Chinese
     * en: English
     * </pre>
     * The default is zh. If you need to return results in English, this field is required
     */
    @Builder.Default
    private String acceptLang = DEF_ACCEPT_LANG;

}
