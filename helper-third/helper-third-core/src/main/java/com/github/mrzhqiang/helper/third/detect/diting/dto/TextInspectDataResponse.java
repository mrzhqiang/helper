package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextInspectDataResponse {


    /**
     * pass: 通过。
     * review: 建议⼈⼯审核。
     * block: ⽂本不合规，建议屏蔽。
     */
    private String suggestion;

    /**
     * normal: 正常；
     * politics: 政治敏感；
     * terror: 暴恐违禁；
     * porn: ⽂本⾊情；
     * ad：恶意推⼴；
     * curse: 咒骂敌视；
     * other: 其他违规
     * nonsense: 刷屏灌⽔；
     * customize: ⽤户⾃定义。
     */
    private String label;

}
