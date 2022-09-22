package com.github.mrzhqiang.helper.third.core;

import com.github.mrzhqiang.helper.Environments;
import com.google.common.base.MoreObjects;
import okhttp3.logging.HttpLoggingInterceptor;

import java.nio.file.Paths;
import java.time.Duration;

/**
 * Okhttp 属性。
 */
public final class OkHttpProperties {

    private final static String DEF_CACHE_PATH = Paths.get(Environments.USER_DIR, ".cache").toString();
    /**
     * 1 GB = 1024 MB = 1024 * 1024 KB = 1024 1024 * 1024 B
     */
    private final static long DEF_CACHE_MAX_SIZE = 1024 * 1024 * 1024;
    private static final Duration DEF_CALL_TIMEOUT = Duration.ofSeconds(5);

    /**
     * OKHttp 的缓存路径。
     */
    private String cachePath = DEF_CACHE_PATH;
    /**
     * OkHttp 的缓存最大值。
     */
    private long cacheMaxSize = DEF_CACHE_MAX_SIZE;
    /**
     * OkHttp 的调用超时。
     * <p>
     * 调用超时跨越整个调用：解析 DNS、连接、写入请求正文、服务器处理和读取响应正文。
     */
    private Duration callTimeout = DEF_CALL_TIMEOUT;
    /**
     * OkHttp 的日志等级。
     * <p>
     * NONE 性能最好，但不输出任何信息。
     * <p>
     * BASIC 只打印请求和响应信息。
     * <p>
     * BODY 输出完整的请求内容和响应体。
     * <p>
     * 常用的就是这三个，具体细节请参考其 API 描述。
     */
    private HttpLoggingInterceptor.Level loggingLevel = HttpLoggingInterceptor.Level.NONE;

    public String getCachePath() {
        return cachePath;
    }

    public void setCachePath(String cachePath) {
        this.cachePath = cachePath;
    }

    public long getCacheMaxSize() {
        return cacheMaxSize;
    }

    public void setCacheMaxSize(long cacheMaxSize) {
        this.cacheMaxSize = cacheMaxSize;
    }

    public Duration getCallTimeout() {
        return callTimeout;
    }

    public void setCallTimeout(Duration callTimeout) {
        this.callTimeout = callTimeout;
    }

    public HttpLoggingInterceptor.Level getLoggingLevel() {
        return loggingLevel;
    }

    public void setLoggingLevel(HttpLoggingInterceptor.Level loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("cachePath", cachePath)
                .add("cacheMaxSize", cacheMaxSize)
                .add("callTimeout", callTimeout)
                .add("loggingLevel", loggingLevel)
                .toString();
    }
}
