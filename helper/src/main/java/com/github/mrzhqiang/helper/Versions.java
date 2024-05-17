package com.github.mrzhqiang.helper;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;

import java.util.stream.Collectors;

/**
 * 版本工具。
 */
public final class Versions {

    private Versions() {
        throw new AssertionError();
    }

    /**
     * 版本分隔符号。
     */
    public static final String DOT = ".";
    /**
     * 版本分离器。
     */
    public static final Splitter VERSION_SPLITTER = Splitter.on(DOT).omitEmptyStrings().trimResults();
    /**
     * 默认版本。
     * <p>
     * 这个是版本的示例值。
     */
    public static final String DEF_VERSION = "1.0.0";

    /**
     * 是否小于。
     *
     * @param appVersion    当前版本。
     * @param targetVersion 目标版本。
     * @return 返回 true 表示当前版本小于目标宝宝，否则不是。
     */
    public static boolean lessThan(String appVersion, String targetVersion) {
        // if appVersion is 1.0.0 >>> 000100000000 >>> 100000000L
        long appVersions = parseVersion(appVersion);
        // if targetVersion is 1.1.0 >>> 000100010000 >>> 100010000L
        long targetVersions = parseVersion(targetVersion);
        // 100000000L < 100010000L is true
        return appVersions < targetVersions;
    }

    private static long parseVersion(String appVersion) {
        return Long.parseLong(VERSION_SPLITTER.splitToStream(appVersion)
                                      .map(it -> Strings.padStart(it, 4, '0'))
                                      .collect(Collectors.joining()));
    }

    /**
     * 是否等于。
     *
     * @param appVersion    当前版本。
     * @param targetVersion 目标版本。
     * @return 返回 true 表示当前版本等于目标版本；否则不是。
     */
    public static boolean equals(String appVersion, String targetVersion) {
        // if appVersion is 1.0.0 >>> 000100000000 >>> 100000000L
        long appVersions = parseVersion(appVersion);
        // if targetVersion is 1.1.0 >>> 000100010000 >>> 100010000L
        long targetVersions = parseVersion(targetVersion);
        // 100000000L == 100010000L is false
        return appVersions == targetVersions;
    }

    /**
     * 是否小于等于。
     *
     * @param appVersion    当前版本。
     * @param targetVersion 目标版本。
     * @return 返回 true 表示当前版本小于等于目标版本；否则不是。
     */
    public static boolean lte(String appVersion, String targetVersion) {
        // if appVersion is 1.0.0 >>> 000100000000 >>> 100000000L
        long appVersions = parseVersion(appVersion);
        // if targetVersion is 1.1.0 >>> 000100010000 >>> 100010000L
        long targetVersions = parseVersion(targetVersion);
        // 100000000L <= 100010000L is true
        return appVersions <= targetVersions;
    }

    /**
     * 是否大于。
     *
     * @param appVersion    当前版本。
     * @param targetVersion 目标版本。
     * @return 返回 true 表示当前版本大于目标版本；否则不是。
     */
    public static boolean greaterThan(String appVersion, String targetVersion) {
        // if appVersion is 1.0.0 >>> 000100000000 >>> 100000000L
        long appVersions = parseVersion(appVersion);
        // if targetVersion is 1.1.0 >>> 000100010000 >>> 100010000L
        long targetVersions = parseVersion(targetVersion);
        // 100000000L > 100010000L is false
        return appVersions > targetVersions;
    }

    /**
     * 是否大于等于。
     *
     * @param appVersion    当前版本。
     * @param targetVersion 目标版本。
     * @return 返回 true 表示当前版本大于等于目标版本；否则不是。
     */
    public static boolean gte(String appVersion, String targetVersion) {
        // if appVersion is 1.0.0 >>> 000100000000 >>> 100000000L
        long appVersions = parseVersion(appVersion);
        // if targetVersion is 1.1.0 >>> 000100010000 >>> 100010000L
        long targetVersions = parseVersion(targetVersion);
        // 100000000L >= 100010000L is false
        return appVersions >= targetVersions;
    }

}
