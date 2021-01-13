package com.github.mrzhqiang.helper.math;

import com.google.common.base.Strings;

import java.math.BigDecimal;

/**
 * 数字工具类。
 */
public enum Numbers {
    ;

    /**
     * <pre>
     *     ofInt(null) >> 0
     *     ofInt("")   >> 0
     *     ofInt("1")  >> 1
     * </pre>
     */
    public static int ofInt(String number) {
        return ofInt(number, 0);
    }

    /**
     * <pre>
     *     ofInt(null,0) >> 0
     *     ofInt("",1)   >> 1
     *     ofInt("1",0)  >> 1
     * </pre>
     */
    public static int ofInt(String number, int defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofLong(null) >> 0L
     *     ofLong("")   >> 0L
     *     ofLong("1")  >> 1L
     * </pre>
     */
    public static long ofLong(String number) {
        return ofLong(number, 0L);
    }

    /**
     * <pre>
     *     ofLong(null,0L) >> 0L
     *     ofLong("",1L)   >> 1L
     *     ofLong("1",0L)  >> 1L
     * </pre>
     */
    public static long ofLong(String number, long defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofFloat(null)    >> 0.0f
     *     ofFloat("")      >> 0.0f
     *     ofFloat("1.0")   >> 1.0f
     * </pre>
     */
    public static float ofFloat(String number) {
        return ofFloat(number, 0.0f);
    }

    /**
     * <pre>
     *     ofFloat(null,0.0f) >> 0.0f
     *     ofFloat("",1.0f)   >> 1.0f
     *     ofFloat("1",0.0f)  >> 1.0f
     * </pre>
     */
    public static float ofFloat(String number, float defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofDouble(null) >> 0.0d
     *     ofDouble("")   >> 0.0d
     *     ofDouble("1")  >> 1.0d
     * </pre>
     */
    public static double ofDouble(String number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * <pre>
     *     ofDouble(null,0.0d) >> 0.0d
     *     ofDouble("",1.0d)   >> 1.0d
     *     ofDouble("1",0.0d)  >> 1.0d
     * </pre>
     */
    public static double ofDouble(String number, double defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   ofDouble(null)                     >> 0.0d
     *   ofDouble(BigDecimal.valueOf(1.0d)) >> 1.0d
     * </pre>
     */
    public static double ofDouble(BigDecimal number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * <pre>
     *   ofDouble(null, 1.1d)                     >> 1.1d
     *   ofDouble(BigDecimal.valueOf(1.0d), 1.1d) >> 1.0d
     * </pre>
     */
    public static double ofDouble(BigDecimal number, double defaultValue) {
        return number == null ? defaultValue : number.doubleValue();
    }

    /**
     * <pre>
     *   ofByte(null) = 0
     *   ofByte("")   = 0
     *   ofByte("1")  = 1
     * </pre>
     */
    public static byte ofByte(String numer) {
        return ofByte(numer, (byte) 0);
    }

    /**
     * <pre>
     *   ofByte(null, 1) = 1
     *   ofByte("", 1)   = 1
     *   ofByte("1", 0)  = 1
     * </pre>
     */
    public static byte ofByte(String number, byte defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Byte.parseByte(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   ofShort(null) = 0
     *   ofShort("")   = 0
     *   ofShort("1")  = 1
     * </pre>
     */
    public static short ofShort(String number) {
        return ofShort(number, (short) 0);
    }

    /**
     * <pre>
     *   ofShort(null, 1) = 1
     *   ofShort("", 1)   = 1
     *   ofShort("1", 0)  = 1
     * </pre>
     */
    public static short ofShort(String number, short defaultValue) {
        if (number == null) {
            return defaultValue;
        }

        try {
            return Short.parseShort(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
