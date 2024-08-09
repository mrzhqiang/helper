package com.github.mrzhqiang.helper.third.detect.diting.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageInspectRequestTask {

    /**
     * 待检测图像的URL
     */
    private String url;

    /**
     * 被检测单个图像对应的数据ID
     * 由⼤⼩写英⽂字⺟、数字、下划线、短划线（-）、英⽂句号组
     * 成，不超过128个字符，⽤于唯⼀标识您的业务数据, 单次检测中不
     * 可重复
     */
    private String data_id;

    private String user_id;

}
