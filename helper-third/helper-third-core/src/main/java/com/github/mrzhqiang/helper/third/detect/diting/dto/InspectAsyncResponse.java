package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InspectAsyncResponse {
    private int code;
    private String msg;

    /**
     * 检测任务对应ID
     */
    private String task_id;
}
