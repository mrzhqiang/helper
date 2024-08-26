package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 异步的 Next Data 视频数据。
 * <pre>
 * {
 *     "code": 1100,
 *     "message": "success",
 *     "requestId": "de407ecdcee333c37dd629fa062de806",
 *     "btId": "1111",
 *     "riskLevel": "PASS",
 *     "auxInfo": {
 *         "billingAudioDuration": 64.8533125,
 *         "billingImgNum": 14,
 *         "frameCount": 0,
 *         "time": 64
 *     }
 * }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/video#Query%20Video%20Results">Query Video Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsyncNextDataVideoData {

    /**
     * <pre>
     * 1100：Success
     * 1901：Request Limit Exceeded
     * 1902：Invalid Parameters
     * 1903：Internal Server Error
     * 9100：Balance Not Enough
     * 9101：Operation Denied
     * </pre>
     */
    private Integer code;
    /**
     * Corresponding to code,
     * include:
     * <pre>
     * Success
     * Request Limit Exceeded
     * Invalid Parameters
     * Internal Server Error
     * Balance Not Enough
     * Operation Denied
     * </pre>
     */
    private String message;
    /**
     * The unique identifier of this request data is used for troubleshooting and effect optimization. It is strongly recommended to save it
     */
    private String requestId;
    /**
     * Up to 64 characters
     */
    private String btId;
    /**
     * Possible return values:
     * <pre>
     * {@code PASS}: Normal, direct release recommended
     * {@code REVIEW}: Suspicious, manual review recommended
     * {@code REJECT}: Violation, direct interception is recommended
     * </pre>
     */
    private String riskLevel;

    public boolean isSuccess() {
        return NextDataResultData.CODE_SUCCESS == code;
    }

    public boolean isHandling() {
        return AsyncNextDataQueryData.CODE_HANDLING == code;
    }

}
