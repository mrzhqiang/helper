package com.github.mrzhqiang.helper.third.translate;

import io.reactivex.Single;
import retrofit2.http.POST;

public interface AzureTranslateAuthApi {

    /**
     * 获取 Token 用于认证。
     *
     * @return 单个反应结果，只有成功或者失败回调。
     */
    @POST("/sts/v1.0/issueToken")
    Single<String> issueToken();

}
