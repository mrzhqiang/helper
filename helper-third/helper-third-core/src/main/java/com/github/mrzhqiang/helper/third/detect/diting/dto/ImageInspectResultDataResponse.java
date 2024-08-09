package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectResultDataResponse {

    private int code;


    private String msg;


    private String data_id;

    /**
     * pass:通过，未检测到命中项
     * review:结果不确定，需要⼈⼯审核
     * block:结果违规，建议采取限制措施
     */
    private String suggestion;

    /**
     *  ⼀级命中标签, 取值如下:
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
     * 处理图⽚数量
     */
    private String subLabel;

    /**
     * 命中原因, 取值如下:
     * 0 : ⽆⻛险
     * 1 : 图⽚中⽂本违规
     * 2 : 图⽚视觉内容违规
     */
    private int image_num;

    /**
     * 命中原因, 取值如下:
     * 0 : ⽆⻛险
     * 1 : 图⽚中⽂本违规
     * 2 : 图⽚视觉内容违规
     */
    private int violation_type;

    private String user_id;

    private String ip;

    private Object face_data;

    private Object detail;

    private String ocr_data;

}
