package com.github.mrzhqiang.helper.third.core;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * 下载接口。
 */
public interface DownloadApi {

    /**
     * 从指定 URL 下载文件。
     *
     * @param url 文件 URL 地址。
     * @return 文件流。
     */
    @GET
    Call<ResponseBody> download(@Url String url);

}
