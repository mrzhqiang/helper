package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataQueryData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NextDataQueryApi {

    @POST("/v4/image/query")
    Call<AsyncNextDataQueryData> queryImage(@Body AsyncNextDataImageBody body);

}
