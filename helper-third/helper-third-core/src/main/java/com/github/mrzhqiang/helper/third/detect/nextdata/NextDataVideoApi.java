package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataVideoBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.AsyncNextDataVideoData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataResultVideoData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataVideoBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NextDataVideoApi {

    @POST("/video/v4")
    Call<NextDataResultVideoData> detectVideo(@Body NextDataVideoBody body);

    @POST("/video/query/v4")
    Call<AsyncNextDataVideoData> queryVideo(@Body AsyncNextDataVideoBody body);

}
