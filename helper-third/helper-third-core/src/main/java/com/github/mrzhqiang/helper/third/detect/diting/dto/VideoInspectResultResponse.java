package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInspectResultResponse {
    private int code;
    private String msg;

    /**
     * 任务ID
     */
    private String task_id;

    /**
     * 被检测视频对应的数据ID
     */
    private String data_id;

    /**
     * 建议您执⾏的后续操作
     * pass:通过，未检测到命中项
     * review:结果不确定，需要⼈⼯审核
     * block:结果违规，建议采取限制措施
     */
    private String suggestion;

    /**
     * 视频截帧总数
     */
    private int frame_count;

    /**
     * 视频时长
     */
    private int duration;

    private String user_id;

    private String ip;


    private List<VideoInspectResultImageResponse> image_detail;

    private List<VideoInspectResultAudioResponse> audio_detail;
}
