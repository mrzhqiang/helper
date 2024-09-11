package com.github.mrzhqiang.helper.third.detect.nextdata;

import com.github.mrzhqiang.helper.third.detect.nextdata.data.BatchNextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.BatchNextDataImageData;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataImageBody;
import com.github.mrzhqiang.helper.third.detect.nextdata.data.NextDataResultData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface NextDataImageApi {

    @POST("/image/v4")
    Call<NextDataResultData> detectImage(@Body NextDataImageBody body);

    @POST("/images/v4")
    Call<BatchNextDataImageData> batchDetectImage(@Body BatchNextDataImageBody body);

}
