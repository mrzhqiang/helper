package com.github.mrzhqiang.helper.third.detect.nextdata.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 异步的 Next Data 图片请求体。
 * <pre>
 *     {
 *     "accessKey": "GUFEpCzZMgpb26CuOvi0",
 *     "requestIds": [
 *         {
 *             "requestId": "ec12cdabf2f8ff97936818aae0ca4024",
 *             "btId": "111"
 *         },
 *         {
 *             "requestId": "ec12cdabf2f8ff97936818aae0ca4024",
 *             "btId": "222"
 *         }
 *     ]
 *     }
 * </pre>
 *
 * @see <a href="https://nextdata.ai/help/text#Return%20results">Text Return Results</a>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AsyncNextDataImageBody {

    /**
     * Company key: used for authority authentication.
     * When opening the account service, it is provided by Sumi or checked at the relevant documents
     * in the upper right corner of the background of Sumi by using the opening mailbox
     */
    private String accessKey;
    /**
     * Query list, supporting up to 10 serial numbers
     */
    private List<RequestId> requestIds;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class RequestId {

        /**
         * The requestId of the query
         */
        private String requestId;
        /**
         * If there is a btId, the single result in the batch query of requestId+btId is returned;
         * If there is no btId, it can be divided into two situations:
         * the first is to return the result of a single request;
         * In the second case, fuzzy matching returns the results of batch requests
         */
        private String btId;

    }

}
