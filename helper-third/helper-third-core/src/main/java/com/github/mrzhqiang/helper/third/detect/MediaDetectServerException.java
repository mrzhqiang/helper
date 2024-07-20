package com.github.mrzhqiang.helper.third.detect;

/**
 * 媒体检测服务异常。
 * <p>
 * 服务异常表示第三方服务不可用，而不是检测失败或者内容不通过。
 */
public final class MediaDetectServerException extends RuntimeException {

    public MediaDetectServerException(String message) {
        super(message);
    }

}
