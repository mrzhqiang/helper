package com.github.mrzhqiang.helper.third.detect;

import com.google.common.collect.ImmutableList;
import lombok.Getter;

import java.util.List;


/**
 * 检测风险等级。
 * <p>
 * pass:通过，未检测到命中项
 * <p>
 * review:结果不确定，需要人工审核
 * <p>
 * block:结果违规，建议采取限制措施
 * <p>
 * 注意：不同供应商返回的风险等级或者建议不同，因此这里采用数组来兼容不同供应商的内容，并使用是否包含的方法来检测风险等级或建议是否一致。
 */
@Getter
public enum DetectRiskLevel {

    PASS(ImmutableList.of("pass", "PASS"), "Normal content, recommended to be released directly"),
    REVIEW(ImmutableList.of("review", "REVIEW"), "Suspicious content, recommended for manual review"),
    BLOCK(ImmutableList.of("block", "REJECT"), "Violation content, recommended to be blocked directly"),
    ;

    private final List<String> levels;
    private final String desc;

    DetectRiskLevel(List<String> levels, String desc) {
        this.levels = levels;
        this.desc = desc;
    }

    public boolean eq(String suggestion) {
        return levels.contains(suggestion);
    }

}
