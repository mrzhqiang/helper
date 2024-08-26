package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 批量的 Next Data 图片数据。
 * <pre>
 *     {
 *     "auxInfo": {},
 *     "code": 1100,
 *     "imgs": [
 *         {
 *             "allLabels": [],
 *             "auxInfo": {
 *                 "segments": 1,
 *                 "typeVersion": {}
 *             },
 *             "btId": "222",
 *             "code": 1100,
 *             "finalResult": 1,
 *             "message": "成功",
 *             "requestId": "c979a73b125b39f25831d10ca4a43611_222",
 *             "resultType": 0,
 *             "riskDescription": "正常",
 *             "riskDetail": {
 *                 "riskSource": 1000
 *             },
 *             "riskLabel1": "normal",
 *             "riskLabel2": "",
 *             "riskLabel3": "",
 *             "riskLevel": "PASS",
 *             "tokenLabels": {
 *                 "UGC_account_risk": {}
 *             }
 *         },
 *         {
 *             "allLabels": [],
 *             "auxInfo": {
 *                 "segments": 1,
 *                 "typeVersion": {}
 *             },
 *             "btId": "111",
 *             "code": 1100,
 *             "finalResult": 1,
 *             "message": "成功",
 *             "requestId": "c979a73b125b39f25831d10ca4a43611_111",
 *             "resultType": 0,
 *             "riskDescription": "正常",
 *             "riskDetail": {
 *                 "riskSource": 1000
 *             },
 *             "riskLabel1": "normal",
 *             "riskLabel2": "",
 *             "riskLabel3": "",
 *             "riskLevel": "PASS",
 *             "tokenLabels": {
 *                 "UGC_account_risk": {}
 *             }
 *         }
 *     ],
 *     "message": "成功",
 *     "requestId": "c979a73b125b39f25831d10ca4a43611"
 *     }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/text#Return%20results">Text Return Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BatchNextDataImageData {

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
     * Picture recognition results
     */
    private List<ImageResult> imgs;

    public boolean isSuccess() {
        return NextDataResultData.CODE_SUCCESS == code;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class ImageResult extends NextDataResultData {

        private String btId;

    }

}
