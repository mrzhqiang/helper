package com.github.mrzhqiang.helper.third.detect;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 媒体检测数据。
 * <p>
 * 通常是第三方接口返回的数据，目前以 DiTing 供应商为主，因为它是第一个接入的供应商。
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MediaDetectData {

    private String mediaId;

    private String dataId;
    /**
     * 建议。
     * <p>
     * 通常的建议分为：pass 通过、review 人工复检、block 阻止。
     * <p>
     * 由于存在多个厂商，所以可能存在不同的数据响应格式，可以通过 {@link JsonAlias} 解决字段名称不一致的问题。
     */
    @JsonAlias({"suggestion"})
    private String suggestion;
    /**
     * 标签。
     * <p>
     * 通常表示建议之外的内容，比如：
     * <p>
     * normal 正常、politics 政治敏感、terror 暴恐违禁、porn 文本色情、ad 恶意推广、curse 咒骂敌视、other 其他违规、
     * nonsense 刷屏灌水、customize 用户自定义。
     */
    @JsonAlias({"label"})
    private String label;

    /**
     * 用户 ID。
     * <p>
     * 看旧代码，只是为了打印日志。
     */
    private String userId;

    public boolean isPassed() {
        // di ting is pass
        // next data is PASS
        return DetectRiskLevel.PASS.eq(suggestion);
    }

    public boolean isBlock() {
        return DetectRiskLevel.BLOCK.eq(suggestion);
    }

}
