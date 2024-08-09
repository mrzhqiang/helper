package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInspectResultAudioResponse {

    /**
     * 违规⽚段开始时间, 单位:秒
     */
    private int start_time;

    /**
     * 违规⽚段结束时间, 单位:秒
     */
    private int end_time;

    /**
     * 建议您执⾏的后续操作
     * pass:通过，未检测到命中项
     * review:结果不确定，需要⼈⼯审核
     * block:结果违规，建议采取限制措施
     */
    private String suggestion;

    /**
     * ⼀级命中标签, 取值如下:
     * normal:正常
     * politics:政治敏感
     * porn:⾊情
     * terror: 涉暴
     * ad: ⼴告
     * forbidden: 违规场景
     * qrcode: ⼆维码
     */
    private String label;

    /**
     * ⼆级命中标签
     */
    private String subLabel;

    /**
     * 语⾳检测对应的⽂本信息
     */
    private String text;



}
