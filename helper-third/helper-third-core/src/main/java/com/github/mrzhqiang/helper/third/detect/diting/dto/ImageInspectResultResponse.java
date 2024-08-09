package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectResultResponse {
    private int code;
    private String msg;

    /**
     * 任务ID
     */
    private String task_id;

    /**
     * 具体的结果数据，调⽤成功时（code=200）返回⼀个或者多个传⼊
     * 数据对应的结果，每个元素代表了⼀张图⽚的结果;详情⻅：附表6-
     * results
     */
    private List<ImageInspectResultDataResponse> results;


}
