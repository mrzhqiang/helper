package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VideoInspectResultImageResponse {

    /**
     * 截帧在视频⽂件中的时间，单位为秒
     */
    private int offset;

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
     * 在光学字符识别场景(ocr)下检测到⽂本区域的⽂本内容
     */
    private String ocr_data;

    /**
     * 图⽚中包含暴恐, 涉政内容时，返回识别出来的暴恐涉政⼈脸信
     * 息, 详⻅附表3-face_data
     */
    private Object face_data;

    /**
     * 命中原因, 取值如下:
     * 0 : ⽆⻛险
     * 1 : 图⽚中⽂本违规
     * 2 : 图⽚视觉内容违规
     */
    private int violation_type;

    private Object detail;

}
