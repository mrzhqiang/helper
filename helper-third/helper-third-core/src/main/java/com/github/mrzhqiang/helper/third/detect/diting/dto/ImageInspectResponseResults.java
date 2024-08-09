package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageInspectResponseResults {
    int code;

    String data_id;

    String user_id;

    /**
     * 处理图⽚数量
     */
    int image_num;

    String ip;

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
    String label;

    String msg;

    /**
     * 在光学字符识别场景(ocr)下检测到⽂本区域的⽂本内容
     */
    String ocr_data;

    /**
     * ⼆级命中标签
     */
    String subLabel;

    /**
     * 建议您执⾏的后续操作
     * pass:通过，未检测到命中项
     * review:结果不确定，需要⼈⼯审核
     * block:结果违规，建议采取限制措施
     */
    String suggestion;

    /**
     * 命中原因, 取值如下:
     * 0 : ⽆⻛险
     * 1 : 图⽚中⽂本违规
     * 2 : 图⽚视觉内容违规
     */
    int violation_type;
}
