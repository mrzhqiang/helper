package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Next Data 视频请求体。
 *
 * @see <a href="https://nextdata.ai/help/video#Request%20Parameters:">Video Request Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NextDataVideoBody {

    public static final String DEF_APP_ID = "default";
    public static final String DEF_EVENT_ID = "dynamic";
    public static final String DEF_IMAGE_TYPE = "POLITY_EROTIC_VIOLENT_ADVERT_QRCODE_IMGTEXTRISK";
    public static final String DEF_AUDIO_TYPE = "POLITY_EROTIC_ADVERT_MOAN_DIRTY";
    public static final String DEF_ACCEPT_LANG = "en";
    public static final String DEF_LANG = "en";

    /**
     * Provided by NEXT DATA and allocated by NEXT DATA
     */
    private String accessKey;
    /**
     * Used to distinguish applications. Contact NEXT DATA for service activation. Please use the value provided by NEXT DATA separately
     */
    @Builder.Default
    private String appId = DEF_APP_ID;
    /**
     * Used to distinguish scenario data. Contact NEXT DATA for service activation. Please use the value provided by NEXT DATA separately
     */
    @Builder.Default
    private String eventId = DEF_EVENT_ID;
    /**
     * Level one label of regulation
     * <p>
     * Optional values:
     * <pre>
     * POLITY: Political recognition
     * EROTIC: Pornographic and sexy violation recognition
     * VIOLENT: Violence and prohibited recognition
     * QRCODE: QR code recognition
     * ADVERT: Advertisement recognition
     * IMGTEXTRISK: ***Recognition of inappropriate text in images
     * </pre>
     * ***If multiple functions need to be recognized, connect them with underscores, such as POLITY_QRCODE_ADVERT for combination recognition of political, QR code, and advertisement
     */
    @Builder.Default
    private String imgType = DEF_IMAGE_TYPE;
    /**
     * Level one label of regulation
     * <p>
     * Optional values:
     * <pre>
     * POLITICS: Political recognition
     * PORN: Pornographic recognition
     * AD: Advertisement recognition
     * MOAN: Moaning recognition
     * ABUSE: Abuse recognition
     * ANTHEN: National anthem recognition
     * AUDIOPOLITICAL: Audio political
     * NONE: ***No audio detection
     * </pre>
     * ***If combination recognition is required, connect them with underscores, such as POLITICAL_PORN_MOAN for advertisement, pornography, and political recognition
     */
    @Builder.Default
    private String audioType = DEF_AUDIO_TYPE;
    /**
     * Up to 1MB, where data content is as follows
     */
    private VideoData data;
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
    @Data
    public static class VideoData {

        /**
         * Unique identifier of the video, used to query recognition results, up to 64 bits
         */
        private String btId;
        /**
         * URL address of the video to be detected
         */
        private String url;
        /**
         * Unique identifier of the client user account, used for user behavior analysis, it is recommended to pass in the user UID; up to 40 bits
         */
        private String tokenId;
        /**
         * Optional values:
         * <pre>
         * zh: Chinese
         * en: English
         * ar: Arabic
         * </pre>
         * Not passing defaults to Chinese detection
         */
        @Builder.Default
        private String lang = DEF_LANG;
        /**
         * Value range is 0.5~60s; if not passed, the default is to capture one frame every 5 seconds
         */
        @Builder.Default
        private Double detectFrequency = 5.0;

    }

}
