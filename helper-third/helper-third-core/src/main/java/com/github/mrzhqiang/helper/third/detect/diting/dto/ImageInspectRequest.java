package com.github.mrzhqiang.helper.third.detect.diting.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ImageInspectRequest {

    /**
     * 接⼊时对指定项⽬分配的权限验证密钥(16位，仅包
     * 含数字和⼤写字⺟)
     */
    private String token;

    /**
     * 指定该图⽚的检测场景，取值如下：
     * politics:政治敏感检测
     * porn:⾊情检测
     * terror: 涉暴检测
     * ocr: 图⽂违规
     * ad: ⼴告检测
     * forbidden: 违规场景
     * qrcode: ⼆维码检测
     */
    private List<String> scenes;

    private List<ImageInspectRequestTask> tasks;

}
