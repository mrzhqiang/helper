package com.github.mrzhqiang.helper.third.core;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Whois IP 数据。
 * <p>
 * 来自：<a href="http://whois.pconline.com.cn/ipJson.jsp">whois</a> 接口。
 */
public final class WhoisIpData {

    /**
     * 当前 IP 地址。
     */
    private String ip;
    /**
     * 省份。
     */
    private String pro;
    /**
     * 省份代码。
     */
    private String proCode;
    /**
     * 城市。
     */
    private String city;
    /**
     * 城市代码。
     */
    private String cityCode;
    /**
     * 区县。
     */
    private String region;
    /**
     * 区县代码。
     */
    private String regionCode;
    /**
     * 地址 + 运营商。
     */
    private String addr;
    /**
     * 区域名称汇总？
     */
    private String regionNames;
    /**
     * 出错信息。
     */
    private String err;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPro() {
        return pro;
    }

    public void setPro(String pro) {
        this.pro = pro;
    }

    public String getProCode() {
        return proCode;
    }

    public void setProCode(String proCode) {
        this.proCode = proCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getRegionNames() {
        return regionNames;
    }

    public void setRegionNames(String regionNames) {
        this.regionNames = regionNames;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WhoisIpData that = (WhoisIpData) o;
        return Objects.equals(ip, that.ip)
                && Objects.equals(pro, that.pro)
                && Objects.equals(proCode, that.proCode)
                && Objects.equals(city, that.city)
                && Objects.equals(cityCode, that.cityCode)
                && Objects.equals(region, that.region)
                && Objects.equals(regionCode, that.regionCode)
                && Objects.equals(addr, that.addr)
                && Objects.equals(regionNames, that.regionNames)
                && Objects.equals(err, that.err);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, pro, proCode, city, cityCode, region, regionCode, addr, regionNames, err);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ip", ip)
                .add("pro", pro)
                .add("proCode", proCode)
                .add("city", city)
                .add("cityCode", cityCode)
                .add("region", region)
                .add("regionCode", regionCode)
                .add("addr", addr)
                .add("regionNames", regionNames)
                .add("err", err)
                .toString();
    }
}
