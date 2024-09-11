package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataImageData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataImageBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NextDataAsyncImageApi {

    @POST("/v4/saas/async/img")
    Call<AsyncNextDataImageData> asyncDetectImage(@Body NextDataImageBody body);

}
