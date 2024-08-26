package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 批量的 Next Data 图片请求体。
 *
 * @see <a href="https://nextdata.ai/help/image#Request%20parameters:">Image Request Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BatchNextDataImageBody {

    /**
     * When opening account service, it is provided by NextData or checked at the relevant documents in the upper right corner of the background of NextData by using the opening mailbox
     */
    private String accessKey;
    /**
     * If you need to contact us to open, please refer to the value provided by NextData separately
     */
    @Builder.Default
    private String appId = NextDataImageBody.DEF_APP_ID;
    /**
     * If you need to contact us to open, please refer to the value provided by NextData separately
     */
    @Builder.Default
    private String eventId = NextDataImageBody.DEF_EVENT_ID;
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
    private String type = NextDataImageBody.DEF_TYPE;
    /**
     * The length of the requested data field is up to 10MB,See data parameter for details
     */
    private BatchData data;
    /**
     * Optional language type of returned information:
     * <pre>
     * zh: Chinese
     * en: English
     * </pre>
     * The default is zh. If you need to return results in English, this field is required
     */
    @Builder.Default
    private String acceptLang = NextDataImageBody.DEF_ACCEPT_LANG;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class BatchData {

        /**
         * Array of pictures to detect
         */
        private List<ImageData> imgs;
        /**
         * Used to distinguish user accounts. It is recommended to pass in user IDs
         */
        private String tokenId;

    }

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
         * It is recommended that the pixel size of the picture be no less than 256 * 256.
         * At present, the minimum resolution of the picture is 20 * 20, and the maximum size of the picture is 10MB
         * <p>
         * By default, the long image is not split. If necessary, please contact Digital America to open it.
         * <p>
         * The charge after splitting is subject to the actual number of frames intercepted.
         */
        private String img;
        /**
         * The same request cannot be repeated, and the length of btId is within 30
         */
        private String btId;

    }

}
