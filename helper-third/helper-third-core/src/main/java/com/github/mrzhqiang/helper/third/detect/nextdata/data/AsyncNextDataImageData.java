package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 异步的 Next Data 图片数据。
 * <p>
 * 只支持单个异步请求，且硅谷的 API 接口不支持异步查询。
 * <pre>
 * {
 *     "code": 1100,
 *     "message": "成功",
 *     "requestId": "ec12cdabf2f8ff97936818aae0ca4024"
 * }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/text#Return%20results">Text Return Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AsyncNextDataImageData {

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
     * Single picture serial numbers
     */
    private String requestId;

    public boolean isSuccess() {
        return NextDataResultData.CODE_SUCCESS == code;
    }

}
