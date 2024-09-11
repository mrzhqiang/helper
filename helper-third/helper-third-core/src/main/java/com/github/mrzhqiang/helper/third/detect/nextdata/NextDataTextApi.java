package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataResultData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataTextBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NextDataTextApi {

    @POST("/text/v4")
    Call<NextDataResultData> detectText(@Body NextDataTextBody body);

}
