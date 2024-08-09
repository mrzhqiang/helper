package com.github.mrzhqiang.helper.third.detect.diting.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class InspectAsyncResultRequest {

    private String token;

    /**
     * 要查询的检测任务的task_id
     */
    private String task_id;


}
