package com.github.mrzhqiang.helper.third.detect.diting.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TextInspectResponse {

    /**
     * 200 请求成功
     * 401 校验失败，请检查参数是否有误
     * 403 ⾮法请求, 请检查加密⽅式是否有误
     * 404 接⼝请求错误, 请检查请求地址
     * 405 请求⽅式错误, 请使⽤ post ⽅式发送请求
     * 500 ⼀般是服务端临时出错。建议重试，若持续返回该错误码，请联系我们。
     */
    private int code;

    /**
     * 请求异常时，可根据此错误信息进⾏排查。msg:
     * data_id is required! : data_id为空
     * context is required! : context为空
     * context_type is required! : context_type为空
     * token is required! : token为空
     * token is illegal! : token不合法
     * the account balance is insufficient, please
     * recharge : 余额不⾜
     * Send a POST request please! : 请求⽅式错误
     * success : 成功
     */
    private String msg;
    private String data_id;

    private TextInspectDataResponse data;
}
