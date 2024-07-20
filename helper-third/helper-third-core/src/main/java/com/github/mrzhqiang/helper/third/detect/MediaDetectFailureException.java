package com.github.mrzhqiang.helper.third.detect;

/**
 * 媒体检测失败异常。
 * <p>
 * 检测失败可能有多种情况，比如内容不符合规范，文件大小超标等等。
 */
public final class MediaDetectFailureException extends RuntimeException {

    public MediaDetectFailureException(String message) {
        super(message);
    }

}
