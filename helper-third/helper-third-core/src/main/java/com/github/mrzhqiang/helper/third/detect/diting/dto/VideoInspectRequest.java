package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class VideoInspectRequest {

    private String token;

    /**
     * 指定该视频的检测场景，取值如下：
     * politics:政治敏感检测
     * porn:⾊情检测
     * terror: 涉暴检测
     * ocr: 图⽂违规
     * ad: ⼴告检测
     * forbidden: 违规场景
     * qrcode: ⼆维码检测
     */
    private List<String> image_scenes;

    /**
     * 指定该视频中⾳频的检测场景，取值如
     * 下：
     * politics:政治敏感检测
     * porn:⾊情检测
     * terror: 涉暴检测
     * curse: 咒骂敌视检测
     * ad: ⼴告检测
     * moan: 娇喘识别
     * 不传则不检测⾳频
     */
    private List<String> audio_scenes;

    private ObjectNode task;

}
