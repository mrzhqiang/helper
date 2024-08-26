package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Next Data 文本请求体。
 *
 * @see <a href="https://nextdata.ai/help/text#Request%20parameters:">Text Request Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NextDataTextBody {

    private static final String DEF_APP_ID = "default";
    private static final String DEF_EVENT_ID = "dynamic";
    public static final String NICKNAME_EVENT_ID = "nickname";
    // VIOLENT_EROTIC
    private static final String DEF_TYPE = "TEXTRISK";
    private static final String DEF_ACCEPT_LANG = "en";
    private static final String DEF_DETECT_LANG = "auto";

    /**
     * Provided by NextData
     */
    private String accessKey;
    /**
     * It is used to distinguish between applications. You need to contact the service of NextData to open. Please use the value transfer provided by NextData separately
     */
    @Builder.Default
    private String appId = DEF_APP_ID;
    /**
     * You need to contact the service of NextData to open. Please use the value transfer provided by NextData separately
     */
    @Builder.Default
    private String eventId = DEF_EVENT_ID;
    /**
     * Optional values:
     * <pre>
     * TEXTRISK：Routine risk detection (including:
     * politics, violence, terrorism, prohibition, pornography, abuse, advertising, privacy, advertising law)
     * FRUAD：Network fraud detection
     * UNPOACH：Anti-digging detection for high-value users
     * TEXTMINOR: Content detection of minors
     * The above types can be combined with underscores, such as:TEXTRISK_FRUAD
     * </pre>
     */
    @Builder.Default
    private String type = DEF_TYPE;
    /**
     * Up to 1MB, see {@link Data} parameter for details
     */
    private Data data;
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @lombok.Data
    public static class Data {

        /**
         * When the value of lang field is zh, or the value of auto is recognized as Chinese,
         * the maximum number of characters in a single request is 10000 characters,
         * and an error will be reported when the number exceeds 10000 characters
         * If the nickname field is passed, the text+nickname content will be verified at the same time.
         */
        private String text;
        /**
         * A string composed of numbers, letters, underscores, and dashes with a length less than or equal to 64 bits
         */
        private String tokenId;
        /**
         * The optional values and corresponding languages are as follows:
         * <pre>
         * zh：Chinese
         * en：English
         * ar : Arabic
         * hi: Hindi
         * es: Spanish
         * fr: French
         * ru: Russian
         * pt: Portuguese
         * id: Indonesian
         * de: German
         * ja: Japanese
         * tr: Turkish
         * vi: Vietnamese
         * it: Italian
         * th: Thai
         * tl: Filipino
         * ko: Korean
         * ms: Malay
         * auto: Automatic recognition of language type
         * </pre>
         * The default value is zh.
         * If you transfer data to domestic colonies, you can choose not to transfer it or transfer it to zh;
         * If the content you want to detect cannot distinguish between languages,
         * it is recommended to select auto, and the system will automatically detect the language type.
         */
        @Builder.Default
        private String lang = DEF_DETECT_LANG;

    }

}
