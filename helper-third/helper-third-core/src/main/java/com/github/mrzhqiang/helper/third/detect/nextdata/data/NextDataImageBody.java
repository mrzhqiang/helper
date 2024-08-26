package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Next Data 图片请求体。
 *
 * @see <a href="https://nextdata.ai/help/image#Request%20parameters:">Image Request Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class NextDataImageBody {

    public static final String DEF_APP_ID = "default";
    public static final String DEF_EVENT_ID = "dynamic";
    public static final String HEAD_EVENT_ID = "headImage";
    public static final String DEF_TYPE = "POLITY_EROTIC_VIOLENT_ADVERT_QRCODE_IMGTEXTRISK";
    public static final String DEF_ACCEPT_LANG = "en";
    public static final String DEF_LANG = "en";

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
     * Optional values of supervision level 1 label:
     * <pre>
     * POLITY :Identify political figures, sensitive political events, etc
     * EROTIC :Identify NSFW, pornographic content, etc
     * VIOLENT :Identify violence, terrorists, drugs, guns, etc
     * QRCODE :Identify QR code
     * ADVERT :Identify spams, ads
     * IMGTEXTRISK :Identify OCR violations
     * </pre>
     * If more than one function needs to be identified, connect it by underline,
     * such as {@code POLITY_QRCODE_ADVERT} which means to identify politics, QR code and spams at the same time
     * <p>
     * （Note: This field and the businessType field must select an incoming）
     * <p>
     * Political, pornographic and violent terrorism only include the detection of the violation of the picture itself.
     * If it is necessary to identify the violation of the text in the picture, be sure to transfer in type {@code IMGTEXTRISK}
     */
    @Builder.Default
    private String type = DEF_TYPE;
    /**
     * The length of the requested data field is up to 10MB,See data parameter for details
     */
    private ImageData data;
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
    public static class ImageData {

        /**
         * Supported formats:
         * <pre>
         * jpg，jpeg，png，webp，gif，tiff，tif，heif
         * </pre>
         * It is recommended that the pixel size of the picture should not be less than 256 * 256.
         * Currently, it supports pictures with resolution between 20 * 20~6000 * 6000.
         * The maximum size of the picture is 10MB, and the maximum asynchronous size is 30M
         * <p>
         * By default, the long image is not split. If necessary, please contact Next Data to open it.
         * The charge after splitting is subject to the actual number of frames intercepted.
         * <p>
         * The image to be detected can use base64 encoded image data or image url link
         * <p>
         * It is recommended to download pictures from the CDN origin, and the origin cannot be a single point
         * <p>
         * Risk: If it is not downloaded from the source site, there may be image download failure, resulting in failure to audit
         */
        private String img;
        /**
         * A string composed of numbers, letters, underscores, and dashes with a length less than or equal to 64 bits
         */
        private String tokenId;
        @Builder.Default
        private String lang = DEF_LANG;

    }

}
