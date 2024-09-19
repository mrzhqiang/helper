package com.github.mrzhqiang.helper.third.detect;

import com.github.mrzhqiang.helper.StringUtils;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 嵌套的媒体检测服务。
 * <p>
 * 需要支持多个 API 供应商：
 * <p>
 * 1. <a href="https://dt.xingzheai.cn">DiTing</a>
 * <p>
 * 2. Google
 * <p>
 * 3. Openai
 * <p>
 * 4. AzureOpenAi
 * <p>
 * 5. AmazonAws
 * <p>
 * 6. <a href="https://nextdata.ai/help/api">NextData</a>
 */
@RequiredArgsConstructor
public class NestedMediaDetectService implements MediaDetectService {

    private static final String SUPPLIER_KEY = "system:setting:api:detect:supplier";
    private static final List<String> SUPPLIER_LIST = ImmutableList.of(
            SUPPLIER_TYPE_DI_TING,
            SUPPLIER_TYPE_GOOGLE,
            SUPPLIER_TYPE_OPEN_AI,
            SUPPLIER_TYPE_AZURE,
            SUPPLIER_TYPE_AMAZON_AWS,
            SUPPLIER_TYPE_NEXT_DATA
    );

    private static final Map<String, String> SUPPLIER_TYPE_MAP = Maps.newHashMap();
    private final List<MediaDetectService> services;

    /**
     * 获取当前供应商类型。
     */
    public String getCurrentSupplierType() {
        String bucket = SUPPLIER_TYPE_MAP.get(SUPPLIER_KEY);
        if (!StringUtils.hasText(bucket)) {
            String type = SUPPLIER_TYPE_DI_TING;
            SUPPLIER_TYPE_MAP.put(SUPPLIER_KEY, type);
            return type;
        }
        return bucket;
    }

    /**
     * 设置当前供应商类型。
     *
     * @param type 供应商类型字符串。
     * @return 返回 true 表示设置成功；返回 false 表示设置失败，可能是空白字符串或者是不受支持的供应商类型。
     */
    public boolean setCurrentSupplierType(String type) {
        if (StringUtils.hasText(type) && SUPPLIER_LIST.contains(type)) {
            SUPPLIER_TYPE_MAP.put(SUPPLIER_KEY, type);
            return true;
        }
        return false;
    }

    @Override
    public MediaDetectData detectText(MediaDetectTextRequest request) {
        return findSupportService()
                .map(it -> it.detectText(request))
                .orElse(null);
    }

    @Override
    public MediaDetectData detectImage(MediaDetectUrlRequest request) {
        return findSupportService()
                .map(it -> it.detectImage(request))
                .orElse(null);
    }

    @Override
    public List<MediaDetectData> batchDetectImage(List<MediaDetectUrlRequest> requests) {
        return findSupportService().map(it -> it.batchDetectImage(requests)).orElse(Collections.emptyList());
    }

    @Override
    public MediaDetectAsyncData asyncDetectImage(List<MediaDetectUrlRequest> requests) {
        return findSupportService().map(it -> it.asyncDetectImage(requests)).orElse(null);
    }

    @Override
    public MediaDetectData queryAsyncDetectImage(String taskId) {
        return findSupportService().map(it -> it.queryAsyncDetectImage(taskId)).orElse(null);
    }

    @Override
    public MediaDetectAsyncData asyncDetectVideo(MediaDetectUrlRequest request) {
        return findSupportService().map(it -> it.asyncDetectVideo(request)).orElse(null);
    }

    @Override
    public MediaDetectData queryAsyncDetectVideo(String taskId) {
        return findSupportService().map(it -> it.queryAsyncDetectVideo(taskId)).orElse(null);
    }

    private Optional<MediaDetectService> findSupportService() {
        if (services == null || services.isEmpty()) {
            return Optional.empty();
        }
        return services.stream()
                .filter(it -> it.isSupport(getCurrentSupplierType()))
                .findFirst();
    }

}
