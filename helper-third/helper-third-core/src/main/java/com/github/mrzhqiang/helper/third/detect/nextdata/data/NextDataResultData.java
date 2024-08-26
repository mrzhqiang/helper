package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.mrzhqiang.helper.third.detect.DetectRiskLevel;
import lombok.Data;

/**
 * Next Data 返回数据。
 * <pre>
 *     {
 *     "allLabels": [],
 *     "auxInfo": {},
 *     "businessLabels": [],
 *     "code": 1100,
 *     "finalResult": 1,
 *     "message": "success",
 *     "requestId": "f2f06c7532df0460e69f493e92e3370d",
 *     "resultType": 0,
 *     "riskDescription": "normal",
 *     "riskDetail": {},
 *     "riskLabel1": "normal",
 *     "riskLabel2": "",
 *     "riskLabel3": "",
 *     "riskLevel": "PASS"
 *     }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/text#Return%20results">Text Return Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NextDataResultData {

    public static final int CODE_SUCCESS = 1100;

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
     * Possible return values:
     * <pre>
     * {@code PASS}: Normal, direct release recommended
     * {@code REVIEW}: Suspicious, manual review recommended
     * {@code REJECT}: Violation, direct interception is recommended
     * </pre>
     */
    private String riskLevel;
    /**
     * First risk label,return {@code normal} when riskLevel is {@code PASS}
     */
    private String riskLabel1;
    /**
     * Second risk label,When riskLevel is {@code PASS}, the return value is Null
     */
    private String riskLabel2;
    /**
     * Third risk label,When riskLevel is {@code PASS}, the return value is Null
     */
    private String riskLabel3;
    /**
     * When riskLevel is {@code PASS}, the return value is {@code normal}
     */
    private String riskDescription;

    public boolean isSuccess() {
        return CODE_SUCCESS == code;
    }

    public boolean isPassed() {
        return DetectRiskLevel.PASS.eq(riskLevel);
    }

}
