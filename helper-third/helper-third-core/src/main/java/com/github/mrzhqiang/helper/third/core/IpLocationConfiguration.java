package com.github.mrzhqiang.helper.third.core;

import com.github.mrzhqiang.helper.Classes;
import com.google.common.collect.Lists;
import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import retrofit2.Retrofit;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.Files;

/**
 * IP 地理位置配置。
 * <p>
 * 在系统中，主要是将 IP 地址转换地理空间位置，需要用到本地数据库和第三方 API 请求。
 */
public final class IpLocationConfiguration {

    private static final String DB_FILE = "/GeoLite2-City.mmdb";

    /**
     * 找到 IP 地理位置数据库文件。
     *
     * @return 文件实例。
     * @throws FileNotFoundException 文件未找到异常。
     */
    private static File findDbFile() throws FileNotFoundException {
        ClassLoader cl = Classes.getDefaultClassLoader();
        URL url = (cl != null ? cl.getResource(DB_FILE) : ClassLoader.getSystemResource(DB_FILE));
        String description = "class path resource [" + DB_FILE + "]";
        if (url == null) {
            throw new FileNotFoundException(description + " cannot be resolved to absolute file path because it does not exist");
        }
        return Classes.getFile(url, description);
    }

    /**
     * GEO 数据库读取器。
     * <p>
     * 关于 locales 的解释：需要按照喜欢程度排序的语言列表，从最喜欢到最不喜欢。
     * <p>
     * 关于 CHMCache 的解释：
     * <p>
     * CHM 即 ConcurrentHashMap，表示使用内存缓存数据，重启后丢失。
     * <p>
     * 这个缓存有存储上限，一旦达到上限值，新的数据将只从数据库读取，不走缓存。
     * <p>
     * 可以通过构造器传参修改存储上限，这里我们先用默认值 4096 观察，不满足需求，再通过配置文件传参到这里进行修改。
     * <p>
     * 注意：可用内存决定了缓存空间的大小，应该设置一个开关来保证低配服务器不启用缓存。
     * <p>
     * 或重写缓存节点的实现，改为中间件缓存，比如 Redis 内存数据库。
     */
    public static DatabaseReader geoDatabaseReader() throws Exception {
        return new DatabaseReader.Builder(Files.newInputStream(findDbFile().toPath()))
                .locales(Lists.newArrayList("zh_CN", "en"))
                .withCache(new CHMCache())
                .build();
    }

    public static WhoisApi whoisApi(Retrofit retrofit) {
        return retrofit.newBuilder()
                .baseUrl(WhoisApi.BASE_URL)
                .build()
                .create(WhoisApi.class);
    }
}
