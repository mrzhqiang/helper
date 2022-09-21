package com.github.mrzhqiang.helper;

import com.google.common.base.Strings;
import com.google.common.net.InetAddresses;

import java.util.regex.Pattern;

/**
 * 网络工具。
 * <p>
 * 推荐使用 Guava 框架 net 包下的相关工具：
 * <p>
 * 1. HostAndPort 主机地址和端口
 * <p>
 * 2. HostSpecifier 主机地址
 * <p>
 * 3. InetAddresses IP 地址
 * <p>
 * 4. InternetDomainName 网络域名
 */
public final class Networks {
    private Networks() {
        // no instance
    }

    public static final int MAX_PORT = 65535;
    public static final int MIN_PORT = 0;

    private static final String REGEX_V4_ADDRESS = "([0-9]{1,3}(\\.[0-9]{1,3}){3})";
    private static final String REGEX_V6_ADDRESS = "[a-f0-9]{1,4}(:[a-f0-9]{1,4}){7}";

    public static boolean isPort(int port) {
        return port >= MIN_PORT && port <= MAX_PORT;
    }

    public static boolean isAddressV4(String address) {
        if (Strings.isNullOrEmpty(address)) {
            return false;
        }

        if (!Pattern.matches(REGEX_V4_ADDRESS, address)) {
            return false;
        }

        for (String section : Splitters.DOT.split(address)) {
            try {
                int i = Integer.parseInt(section);
                if (i < 0 || i > 255) {
                    return false;
                }
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAddressV6(String address) {
        if (Strings.isNullOrEmpty(address)) {
            return false;
        }

        if (!Pattern.matches(REGEX_V6_ADDRESS, address)) {
            return false;
        }

        //noinspection UnstableApiUsage
        return InetAddresses.isInetAddress(address);
    }
}
