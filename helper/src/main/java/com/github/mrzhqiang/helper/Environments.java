package com.github.mrzhqiang.helper;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import java.io.File;
import java.util.Objects;

/**
 * 环境变量工具。
 * <p>
 * 实际上可以用 Guava 的 StandardSystemProperty 类替代当前类。
 */
public final class Environments {
    private Environments() {
        // no instance
    }

    public static final String INTELLIJ_DEBUG_AGENT = "intellij.debug.agent";

    /* copy from apache commons lang3 SystemUtils */
    private static final String OS_NAME_WINDOWS_PREFIX = "Windows";

    private static final String USER_HOME_KEY = "user.home";
    private static final String USER_NAME_KEY = "user.name";
    private static final String USER_DIR_KEY = "user.dir";
    private static final String JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
    private static final String JAVA_HOME_KEY = "java.home";

    public static final String AWT_TOOLKIT = getSystemProperty("awt.toolkit");
    public static final String FILE_ENCODING = getSystemProperty("file.encoding");
    public static final String JAVA_AWT_FONTS = getSystemProperty("java.awt.fonts");
    public static final String JAVA_AWT_GRAPHICSENV = getSystemProperty("java.awt.graphicsenv");
    public static final String JAVA_AWT_HEADLESS = getSystemProperty("java.awt.headless");
    public static final String JAVA_AWT_PRINTERJOB = getSystemProperty("java.awt.printerjob");
    public static final String JAVA_CLASS_PATH = getSystemProperty("java.class.path");
    public static final String JAVA_CLASS_VERSION = getSystemProperty("java.class.version");
    public static final String JAVA_COMPILER = getSystemProperty("java.compiler");
    public static final String JAVA_ENDORSED_DIRS = getSystemProperty("java.endorsed.dirs");
    public static final String JAVA_EXT_DIRS = getSystemProperty("java.ext.dirs");
    public static final String JAVA_HOME = getSystemProperty(JAVA_HOME_KEY);
    public static final String JAVA_IO_TMPDIR = getSystemProperty(JAVA_IO_TMPDIR_KEY);
    public static final String JAVA_LIBRARY_PATH = getSystemProperty("java.library.path");
    public static final String JAVA_RUNTIME_NAME = getSystemProperty("java.runtime.name");
    public static final String JAVA_RUNTIME_VERSION = getSystemProperty("java.runtime.version");
    public static final String JAVA_SPECIFICATION_NAME = getSystemProperty("java.specification.name");
    public static final String JAVA_SPECIFICATION_VENDOR = getSystemProperty("java.specification.vendor");
    public static final String JAVA_SPECIFICATION_VERSION = getSystemProperty("java.specification.version");
    public static final String JAVA_UTIL_PREFS_PREFERENCES_FACTORY =
            getSystemProperty("java.util.prefs.PreferencesFactory");
    public static final String JAVA_VENDOR = getSystemProperty("java.vendor");
    public static final String JAVA_VENDOR_URL = getSystemProperty("java.vendor.url");
    public static final String JAVA_VERSION = getSystemProperty("java.version");
    public static final String JAVA_VM_INFO = getSystemProperty("java.vm.info");
    public static final String JAVA_VM_NAME = getSystemProperty("java.vm.name");
    public static final String JAVA_VM_SPECIFICATION_NAME = getSystemProperty("java.vm.specification.name");
    public static final String JAVA_VM_SPECIFICATION_VENDOR = getSystemProperty("java.vm.specification.vendor");
    public static final String JAVA_VM_SPECIFICATION_VERSION = getSystemProperty("java.vm.specification.version");
    public static final String JAVA_VM_VENDOR = getSystemProperty("java.vm.vendor");
    public static final String JAVA_VM_VERSION = getSystemProperty("java.vm.version");

    public static final String OS_ARCH = getSystemProperty("os.arch");
    public static final String OS_NAME = getSystemProperty("os.name");
    public static final String OS_VERSION = getSystemProperty("os.version");

    public static final String USER_COUNTRY =
            MoreObjects.firstNonNull(getSystemProperty("user.country"), getSystemProperty("user.region"));
    public static final String USER_DIR = getSystemProperty(USER_DIR_KEY);
    public static final String USER_HOME = getSystemProperty(USER_HOME_KEY);
    public static final String USER_LANGUAGE = getSystemProperty("user.language");
    public static final String USER_NAME = getSystemProperty(USER_NAME_KEY);
    public static final String USER_TIMEZONE = getSystemProperty("user.timezone");

    public static final boolean IS_OS_LINUX =
            getOsMatchesName("Linux") || getOsMatchesName("LINUX");
    public static final boolean IS_OS_MAC = getOsMatchesName("Mac");
    public static final boolean IS_OS_WINDOWS = getOsMatchesName(OS_NAME_WINDOWS_PREFIX);
    public static final boolean IS_OS_WINDOWS_2000 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2000");
    public static final boolean IS_OS_WINDOWS_2003 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 2003");
    public static final boolean IS_OS_WINDOWS_2008 =
            getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2008");
    public static final boolean IS_OS_WINDOWS_2012 =
            getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Server 2012");
    public static final boolean IS_OS_WINDOWS_95 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 95");
    public static final boolean IS_OS_WINDOWS_98 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 98");
    public static final boolean IS_OS_WINDOWS_ME = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Me");
    public static final boolean IS_OS_WINDOWS_NT = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " NT");
    public static final boolean IS_OS_WINDOWS_XP = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " XP");
    public static final boolean IS_OS_WINDOWS_VISTA =
            getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " Vista");
    public static final boolean IS_OS_WINDOWS_7 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 7");
    public static final boolean IS_OS_WINDOWS_8 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 8");
    public static final boolean IS_OS_WINDOWS_10 = getOsMatchesName(OS_NAME_WINDOWS_PREFIX + " 10");

    public static File getJavaHome() {
        return new File(System.getProperty(JAVA_HOME_KEY));
    }

    public static String getHostName() {
        return IS_OS_WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
    }

    @SuppressWarnings("SameParameterValue")
    static boolean isOSNameMatch(String osName, String osNamePrefix) {
        return Objects.nonNull(osName) && osName.startsWith(osNamePrefix);
    }

    @SuppressWarnings("SameParameterValue")
    static boolean isOSMatch(String osName, String osVersion, String osNamePrefix, String osVersionPrefix) {
        if (osName == null || osVersion == null) {
            return false;
        }
        return isOSNameMatch(osName, osNamePrefix) && isOSVersionMatch(osVersion, osVersionPrefix);
    }

    static boolean isOSVersionMatch(String osVersion, String osVersionPrefix) {
        if (Strings.isNullOrEmpty(osVersion)) {
            return false;
        }
        // Compare parts of the version string instead of using String.startsWith(String) because otherwise
        // osVersionPrefix 10.1 would also match osVersion 10.10
        String[] versionPrefixParts = osVersionPrefix.split("\\.");
        String[] versionParts = osVersion.split("\\.");
        for (int i = 0; i < Math.min(versionPrefixParts.length, versionParts.length); i++) {
            if (!versionPrefixParts[i].equals(versionParts[i])) {
                return false;
            }
        }
        return true;
    }

    public static File getJavaIoTmpDir() {
        return new File(System.getProperty(JAVA_IO_TMPDIR_KEY));
    }

    private static boolean getOsMatchesName(String osNamePrefix) {
        return isOSNameMatch(OS_NAME, osNamePrefix);
    }

    private static String getSystemProperty(String property) {
        try {
            return System.getProperty(property);
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
            // System.err.println("Caught a SecurityException reading the system property '" + property
            // + "'; the SystemUtils property value will default to null.");
            return null;
        }
    }

    public static String getEnvironmentVariable(String name, String defaultValue) {
        try {
            String value = System.getenv(name);
            return value == null ? defaultValue : value;
        } catch (SecurityException ex) {
            // we are not allowed to look at this property
            // System.err.println("Caught a SecurityException reading the environment variable '" + name + "'.");
            return defaultValue;
        }
    }

    public static File getUserDir() {
        return new File(System.getProperty(USER_DIR_KEY));
    }

    public static File getUserHome() {
        return new File(System.getProperty(USER_HOME_KEY));
    }

    public static String getUserName() {
        return System.getProperty(USER_NAME_KEY);
    }

    public static String getUserName(String defaultValue) {
        return System.getProperty(USER_NAME_KEY, defaultValue);
    }

    public static boolean isJavaAwtHeadless() {
        return Boolean.TRUE.toString().equals(JAVA_AWT_HEADLESS);
    }

    /* copy from apache commons lang3 SystemUtils */

    /**
     * Check debug mode.
     * <p>
     * The method works only in IntelliJ IDEA.
     *
     * @return true if the current environment is in debug mode.
     */
    public static boolean debug() {
        return Boolean.parseBoolean(System.getProperty(INTELLIJ_DEBUG_AGENT, Boolean.FALSE.toString()));
    }
}
