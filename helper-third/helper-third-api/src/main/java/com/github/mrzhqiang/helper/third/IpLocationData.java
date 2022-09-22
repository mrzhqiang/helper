package com.github.mrzhqiang.helper.third;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * IP 地理位置数据。
 * <p>
 */
public final class IpLocationData {

    /**
     * IP 地址。
     */
    private String ip;
    /**
     * 地理位置。
     */
    private String location;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IpLocationData that = (IpLocationData) o;
        return Objects.equals(ip, that.ip) && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ip, location);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("ip", ip)
                .add("location", location)
                .toString();
    }
}
