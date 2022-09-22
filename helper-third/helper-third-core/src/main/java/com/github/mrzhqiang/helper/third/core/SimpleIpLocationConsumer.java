package com.github.mrzhqiang.helper.third.core;

import com.github.mrzhqiang.helper.third.IpLocationConsumer;
import com.github.mrzhqiang.helper.third.IpLocationData;
import com.google.common.base.Strings;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CityResponse;
import io.reactivex.Observable;
import io.reactivex.observers.DefaultObserver;
import io.reactivex.schedulers.Schedulers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.util.function.Consumer;

/**
 * 简单的 IP 地理位置转换器。
 * <p>
 */
public final class SimpleIpLocationConsumer implements IpLocationConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleIpLocationConsumer.class);

    private static final String LOCAL_LANGUAGE = "zh-CN";
    private static final String LOCATION_TEMPLATE = "%s %s";
    private static final String UNKNOWN_LOCATION = "(unknown)";

    private final WhoisApi api;
    private final DatabaseReader reader;

    public SimpleIpLocationConsumer(WhoisApi api, DatabaseReader reader) {
        this.api = api;
        this.reader = reader;
    }

    @Override
    public void accept(String ip, Consumer<IpLocationData> consumer) {
        this.observeApi(ip)
                .onErrorResumeNext(this.observeDb(ip))
                .subscribe(new DefaultObserver<IpLocationData>() {
                    @Override
                    public void onNext(@Nonnull IpLocationData data) {
                        consumer.accept(data);
                    }

                    @Override
                    public void onError(@Nonnull Throwable e) {
                        LOGGER.error("无法为 {} 找到对应地址，可能是：{} 问题", ip, e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        // do nothing
                    }
                });
    }

    /**
     * 通过第三方 API 将 IP 转换为包含地理位置的会话详情。
     */
    public Observable<IpLocationData> observeApi(String ip) {
        return api.ipJson(ip, true)
                .subscribeOn(Schedulers.io())
                .map(this::toData);
    }

    /**
     * 从本地数据库寻找 IP 映射的地理位置，作为会话详情数据。
     */
    public Observable<IpLocationData> observeDb(String remoteAddress) {
        //noinspection ReactiveStreamsTooLongSameOperatorsChain
        return Observable.just(remoteAddress)
                .subscribeOn(Schedulers.io())
                .map(InetAddress::getByName)
                .map(reader::city)
                .map(this::toData);
    }

    private IpLocationData toData(WhoisIpData ipData) {
        IpLocationData data = new IpLocationData();
        data.setIp(ipData.getIp());
        data.setLocation(ipData.getAddr());
        return data;
    }

    private IpLocationData toData(CityResponse response) {
        IpLocationData data = new IpLocationData();
        data.setIp(response.getTraits().getIpAddress());
        String cityName = response.getCity().getNames().get(LOCAL_LANGUAGE);
        String countryName = response.getCountry().getNames().get(LOCAL_LANGUAGE);
        String location = UNKNOWN_LOCATION;
        if (!Strings.isNullOrEmpty(countryName) && !Strings.isNullOrEmpty(cityName)) {
            location = Strings.lenientFormat(LOCATION_TEMPLATE, countryName, cityName);
        }
        data.setLocation(location);
        return data;
    }
}
