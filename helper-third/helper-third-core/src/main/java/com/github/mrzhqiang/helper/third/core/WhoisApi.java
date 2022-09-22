package com.github.mrzhqiang.helper.third.core;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

@SuppressWarnings("HttpUrlsUsage")
public interface WhoisApi {

    String BASE_URL = "http://whois.pconline.com.cn";

    @GET("/ipJson.jsp")
    Observable<WhoisIpData> ipJson(@Query("ip") String ip, @Query("json") boolean json);
}
