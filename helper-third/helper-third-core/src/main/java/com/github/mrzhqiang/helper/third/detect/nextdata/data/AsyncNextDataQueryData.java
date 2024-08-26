package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 异步的 Next Data 查询结果数据。
 * <pre>
 * {
 *     "code": 1100,
 *     "message": "成功",
 *     "contents": [
 *         {
 *             "requestId": "748ff0368bf3271158d4555e1ac1b7b7",
 *             "btId": "555",
 *             "code": 1102,
 *             "message": "正在处理"
 *         }
 *     ]
 * }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/text#Return%20results">Text Return Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class AsyncNextDataQueryData {

    public static final int CODE_HANDLING = 1101;

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
     * Multiple picture serial numbers
     */
    private List<Content> contents;

    public boolean isSuccess() {
        return NextDataResultData.CODE_SUCCESS == code;
    }

    public boolean isHandling() {
        return CODE_HANDLING == code;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Content {

        public static final int PROCESS_COMPLETED = 1100;

        /**
         * Returned requestId
         */
        private String requestId;
        /**
         * Return results
         */
        @JsonProperty("Result")
        private NextDataResultData result;
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

        public boolean isCompleted() {
            return PROCESS_COMPLETED == code;
        }

    }

}
