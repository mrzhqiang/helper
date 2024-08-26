package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Next Data 返回的视频数据。
 * <pre>
 * {
 *     "code": 1100,
 *     "message": "success",
 *     "requestId": "f8724fe7aa6a622d2dea7b1ae42bd82c",
 *     "btId": "1111"
 * }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/video#Return%20Parameters:">Video Return Parameters</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NextDataResultVideoData {

    /**
     * See Interface Response Code List
     */
    private Integer code;
    /**
     * Details described above
     */
    private String message;
    /**
     * Request unique identifier
     */
    private String requestId;
    /**
     * Only returned when code=1100, corresponding to the btId field in the request parameters
     */
    private String btId;

    public boolean isSuccess() {
        return NextDataResultData.CODE_SUCCESS == code;
    }

}
