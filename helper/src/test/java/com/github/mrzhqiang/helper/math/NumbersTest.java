package com.github.mrzhqiang.helper.math;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class NumbersTest {

    /**
     * Test for {@link Numbers#ofDouble(BigDecimal)}
     */
    @Test
    public void testBigIntegerToDoubleBigInteger() {
        assertEquals(0.0d, Numbers.ofDouble((BigDecimal) null), 0);
        assertEquals(8.5d, Numbers.ofDouble(BigDecimal.valueOf(8.5d)), 0);
    }

    /**
     * Test for {@link Numbers#ofDouble(BigDecimal, double)}
     */
    @Test
    public void testBigIntegerToDoubleBigIntegerD() {
        assertEquals(1.1d, Numbers.ofDouble((BigDecimal) null, 1.1d), 0);
        assertEquals(8.5d, Numbers.ofDouble(BigDecimal.valueOf(8.5d), 1.1d), 0);
    }

    // Testing JDK against old Lang functionality
    @SuppressWarnings("EqualsWithItself")
    @Test
    public void testCompareDouble() {
        assertEquals(0, Double.compare(Double.NaN, Double.NaN));
        assertEquals(Double.compare(Double.NaN, Double.POSITIVE_INFINITY), +1);
        assertEquals(Double.compare(Double.NaN, Double.MAX_VALUE), +1);
        assertEquals(Double.compare(Double.NaN, 1.2d), +1);
        assertEquals(Double.compare(Double.NaN, 0.0d), +1);
        assertEquals(Double.compare(Double.NaN, -0.0d), +1);
        assertEquals(Double.compare(Double.NaN, -1.2d), +1);
        assertEquals(Double.compare(Double.NaN, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(Double.NaN, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.NaN), -1);
        assertEquals(0, Double.compare(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.MAX_VALUE), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, 1.2d), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, 0.0d), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, -0.0d), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, -1.2d), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(Double.MAX_VALUE, Double.NaN), -1);
        assertEquals(Double.compare(Double.MAX_VALUE, Double.POSITIVE_INFINITY), -1);
        assertEquals(0, Double.compare(Double.MAX_VALUE, Double.MAX_VALUE));
        assertEquals(Double.compare(Double.MAX_VALUE, 1.2d), +1);
        assertEquals(Double.compare(Double.MAX_VALUE, 0.0d), +1);
        assertEquals(Double.compare(Double.MAX_VALUE, -0.0d), +1);
        assertEquals(Double.compare(Double.MAX_VALUE, -1.2d), +1);
        assertEquals(Double.compare(Double.MAX_VALUE, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(Double.MAX_VALUE, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(1.2d, Double.NaN), -1);
        assertEquals(Double.compare(1.2d, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(1.2d, Double.MAX_VALUE), -1);
        assertEquals(0, Double.compare(1.2d, 1.2d));
        assertEquals(Double.compare(1.2d, 0.0d), +1);
        assertEquals(Double.compare(1.2d, -0.0d), +1);
        assertEquals(Double.compare(1.2d, -1.2d), +1);
        assertEquals(Double.compare(1.2d, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(1.2d, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(0.0d, Double.NaN), -1);
        assertEquals(Double.compare(0.0d, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(0.0d, Double.MAX_VALUE), -1);
        assertEquals(Double.compare(0.0d, 1.2d), -1);
        assertEquals(0, Double.compare(0.0d, 0.0d));
        assertEquals(Double.compare(0.0d, -0.0d), +1);
        assertEquals(Double.compare(0.0d, -1.2d), +1);
        assertEquals(Double.compare(0.0d, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(0.0d, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(-0.0d, Double.NaN), -1);
        assertEquals(Double.compare(-0.0d, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(-0.0d, Double.MAX_VALUE), -1);
        assertEquals(Double.compare(-0.0d, 1.2d), -1);
        assertEquals(Double.compare(-0.0d, 0.0d), -1);
        assertEquals(0, Double.compare(-0.0d, -0.0d));
        assertEquals(Double.compare(-0.0d, -1.2d), +1);
        assertEquals(Double.compare(-0.0d, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(-0.0d, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(-1.2d, Double.NaN), -1);
        assertEquals(Double.compare(-1.2d, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(-1.2d, Double.MAX_VALUE), -1);
        assertEquals(Double.compare(-1.2d, 1.2d), -1);
        assertEquals(Double.compare(-1.2d, 0.0d), -1);
        assertEquals(Double.compare(-1.2d, -0.0d), -1);
        assertEquals(0, Double.compare(-1.2d, -1.2d));
        assertEquals(Double.compare(-1.2d, -Double.MAX_VALUE), +1);
        assertEquals(Double.compare(-1.2d, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(-Double.MAX_VALUE, Double.NaN), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, Double.MAX_VALUE), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, 1.2d), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, 0.0d), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, -0.0d), -1);
        assertEquals(Double.compare(-Double.MAX_VALUE, -1.2d), -1);
        assertEquals(0, Double.compare(-Double.MAX_VALUE, -Double.MAX_VALUE));
        assertEquals(Double.compare(-Double.MAX_VALUE, Double.NEGATIVE_INFINITY), +1);

        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.NaN), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, Double.MAX_VALUE), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, 1.2d), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, 0.0d), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -0.0d), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -1.2d), -1);
        assertEquals(Double.compare(Double.NEGATIVE_INFINITY, -Double.MAX_VALUE), -1);
        assertEquals(0, Double.compare(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
    }

    @SuppressWarnings("EqualsWithItself")
    @Test
    public void testCompareFloat() {
        assertEquals(0, Float.compare(Float.NaN, Float.NaN));
        assertEquals(Float.compare(Float.NaN, Float.POSITIVE_INFINITY), +1);
        assertEquals(Float.compare(Float.NaN, Float.MAX_VALUE), +1);
        assertEquals(Float.compare(Float.NaN, 1.2f), +1);
        assertEquals(Float.compare(Float.NaN, 0.0f), +1);
        assertEquals(Float.compare(Float.NaN, -0.0f), +1);
        assertEquals(Float.compare(Float.NaN, -1.2f), +1);
        assertEquals(Float.compare(Float.NaN, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(Float.NaN, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.NaN), -1);
        assertEquals(0, Float.compare(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY));
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.MAX_VALUE), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, 1.2f), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, 0.0f), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, -0.0f), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, -1.2f), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(Float.MAX_VALUE, Float.NaN), -1);
        assertEquals(Float.compare(Float.MAX_VALUE, Float.POSITIVE_INFINITY), -1);
        assertEquals(0, Float.compare(Float.MAX_VALUE, Float.MAX_VALUE));
        assertEquals(Float.compare(Float.MAX_VALUE, 1.2f), +1);
        assertEquals(Float.compare(Float.MAX_VALUE, 0.0f), +1);
        assertEquals(Float.compare(Float.MAX_VALUE, -0.0f), +1);
        assertEquals(Float.compare(Float.MAX_VALUE, -1.2f), +1);
        assertEquals(Float.compare(Float.MAX_VALUE, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(Float.MAX_VALUE, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(1.2f, Float.NaN), -1);
        assertEquals(Float.compare(1.2f, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(1.2f, Float.MAX_VALUE), -1);
        assertEquals(0, Float.compare(1.2f, 1.2f));
        assertEquals(Float.compare(1.2f, 0.0f), +1);
        assertEquals(Float.compare(1.2f, -0.0f), +1);
        assertEquals(Float.compare(1.2f, -1.2f), +1);
        assertEquals(Float.compare(1.2f, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(1.2f, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(0.0f, Float.NaN), -1);
        assertEquals(Float.compare(0.0f, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(0.0f, Float.MAX_VALUE), -1);
        assertEquals(Float.compare(0.0f, 1.2f), -1);
        assertEquals(0, Float.compare(0.0f, 0.0f));
        assertEquals(Float.compare(0.0f, -0.0f), +1);
        assertEquals(Float.compare(0.0f, -1.2f), +1);
        assertEquals(Float.compare(0.0f, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(0.0f, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(-0.0f, Float.NaN), -1);
        assertEquals(Float.compare(-0.0f, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(-0.0f, Float.MAX_VALUE), -1);
        assertEquals(Float.compare(-0.0f, 1.2f), -1);
        assertEquals(Float.compare(-0.0f, 0.0f), -1);
        assertEquals(0, Float.compare(-0.0f, -0.0f));
        assertEquals(Float.compare(-0.0f, -1.2f), +1);
        assertEquals(Float.compare(-0.0f, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(-0.0f, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(-1.2f, Float.NaN), -1);
        assertEquals(Float.compare(-1.2f, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(-1.2f, Float.MAX_VALUE), -1);
        assertEquals(Float.compare(-1.2f, 1.2f), -1);
        assertEquals(Float.compare(-1.2f, 0.0f), -1);
        assertEquals(Float.compare(-1.2f, -0.0f), -1);
        assertEquals(0, Float.compare(-1.2f, -1.2f));
        assertEquals(Float.compare(-1.2f, -Float.MAX_VALUE), +1);
        assertEquals(Float.compare(-1.2f, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(-Float.MAX_VALUE, Float.NaN), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, Float.MAX_VALUE), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, 1.2f), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, 0.0f), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, -0.0f), -1);
        assertEquals(Float.compare(-Float.MAX_VALUE, -1.2f), -1);
        assertEquals(0, Float.compare(-Float.MAX_VALUE, -Float.MAX_VALUE));
        assertEquals(Float.compare(-Float.MAX_VALUE, Float.NEGATIVE_INFINITY), +1);

        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.NaN), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, Float.MAX_VALUE), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, 1.2f), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, 0.0f), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -0.0f), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -1.2f), -1);
        assertEquals(Float.compare(Float.NEGATIVE_INFINITY, -Float.MAX_VALUE), -1);
        assertEquals(0, Float.compare(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY));
    }

    /**
     * Test for {@link Numbers#ofDouble(String)}.
     */
    @Test
    public void testStringToDoubleString() {
        assertEquals(Numbers.ofDouble("-1.2345"), -1.2345d, 0);
        assertEquals(1.2345d, Numbers.ofDouble("1.2345"), 0);
        assertEquals(0.0d, Numbers.ofDouble("abc"), 0);
        // LANG-1060
        assertEquals(Numbers.ofDouble("-001.2345"), -1.2345d, 0);
        assertEquals(1.2345d, Numbers.ofDouble("+001.2345"), 0);
        assertEquals(1.2345d, Numbers.ofDouble("001.2345"), 0);
        assertEquals(0d, Numbers.ofDouble("000.00000"), 0);

        assertEquals(Numbers.ofDouble(Double.MAX_VALUE + ""), Double.MAX_VALUE, 0);
        assertEquals(Numbers.ofDouble(Double.MIN_VALUE + ""), Double.MIN_VALUE, 0);
        assertEquals(0.0d, Numbers.ofDouble(""), 0);
        assertEquals(0.0d, Numbers.ofDouble((String) null), 0);
    }

    /**
     * Test for {@link Numbers#ofDouble(String, double)}.
     */
    @Test
    public void testStringToDoubleStringD() {
        assertEquals(1.2345d, Numbers.ofDouble("1.2345", 5.1d), 0);
        assertEquals(5.0d, Numbers.ofDouble("a", 5.0d), 0);
        // LANG-1060
        assertEquals(1.2345d, Numbers.ofDouble("001.2345", 5.1d), 0);
        assertEquals(Numbers.ofDouble("-001.2345", 5.1d), -1.2345d, 0);
        assertEquals(1.2345d, Numbers.ofDouble("+001.2345", 5.1d), 0);
        assertEquals(0d, Numbers.ofDouble("000.00", 5.1d), 0);
    }

    /**
     * Test for {@link Numbers#ofByte(String)}.
     */
    @Test
    public void testToByteString() {
        assertEquals(123, Numbers.ofByte("123"), 0);
        assertEquals(0, Numbers.ofByte("abc"), 0);
        assertEquals(0, Numbers.ofByte(""), 0);
        assertEquals(0, Numbers.ofByte(null), 0);
    }

    /**
     * Test for {@link Numbers#ofByte(String, byte)}.
     */
    @Test
    public void testToByteStringI() {
        assertEquals(123, Numbers.ofByte("123", (byte) 5), 0);
        assertEquals(5, Numbers.ofByte("12.3", (byte) 5), 0);
    }

    /**
     * Test for {@link Numbers#ofFloat(String)}.
     */
    @Test
    public void testToFloatString() {
        assertEquals(Numbers.ofFloat("-1.2345"), -1.2345f, 0);
        assertEquals(1.2345f, Numbers.ofFloat("1.2345"), 0);
        assertEquals(0.0f, Numbers.ofFloat("abc"), 0);
        // LANG-1060
        assertEquals(Numbers.ofFloat("-001.2345"), -1.2345f, 0);
        assertEquals(1.2345f, Numbers.ofFloat("+001.2345"), 0);
        assertEquals(1.2345f, Numbers.ofFloat("001.2345"), 0);
        assertEquals(0f, Numbers.ofFloat("000.00"), 0);

        assertEquals(Numbers.ofFloat(Float.MAX_VALUE + ""), Float.MAX_VALUE, 0);
        assertEquals(Numbers.ofFloat(Float.MIN_VALUE + ""), Float.MIN_VALUE, 0);
        assertEquals(0.0f, Numbers.ofFloat(""), 0);
        assertEquals(0.0f, Numbers.ofFloat(null), 0);
    }

    /**
     * Test for {@link Numbers#ofFloat(String, float)}.
     */
    @Test
    public void testToFloatStringF() {
        assertEquals(1.2345f, Numbers.ofFloat("1.2345", 5.1f), 0);
        assertEquals(5.0f, Numbers.ofFloat("a", 5.0f), 0);
        // LANG-1060
        assertEquals(5.0f, Numbers.ofFloat("-001Z.2345", 5.0f), 0);
        assertEquals(5.0f, Numbers.ofFloat("+001AB.2345", 5.0f), 0);
        assertEquals(5.0f, Numbers.ofFloat("001Z.2345", 5.0f), 0);
    }

    /**
     * Test for {@link Numbers#ofInt(String)}.
     */
    @Test
    public void testToIntString() {
        assertEquals(12345, Numbers.ofInt("12345"), 0);
        assertEquals(0, Numbers.ofInt("abc"), 0);
        assertEquals(0, Numbers.ofInt(""), 0);
        assertEquals(0, Numbers.ofInt(null), 0);
    }

    /**
     * Test for {@link Numbers#ofInt(String, int)}.
     */
    @Test
    public void testToIntStringI() {
        assertEquals(12345, Numbers.ofInt("12345", 5), 0);
        assertEquals(5, Numbers.ofInt("1234.5", 5), 0);
    }

    /**
     * Test for {@link Numbers#ofLong(String)}.
     */
    @Test
    public void testToLongString() {
        assertEquals(12345L, Numbers.ofLong("12345"), 0);
        assertEquals(0L, Numbers.ofLong("abc"), 0);
        assertEquals(0L, Numbers.ofLong("1L"), 0);
        assertEquals(0L, Numbers.ofLong("1l"), 0);
        assertEquals(Numbers.ofLong(Long.MAX_VALUE + ""), Long.MAX_VALUE, 0);
        assertEquals(Numbers.ofLong(Long.MIN_VALUE + ""), Long.MIN_VALUE, 0);
        assertEquals(0L, Numbers.ofLong(""), 0);
        assertEquals(0L, Numbers.ofLong(null), 0);
    }

    /**
     * Test for {@link Numbers#ofLong(String, long)}.
     */
    @Test
    public void testToLongStringL() {
        assertEquals(12345L, Numbers.ofLong("12345", 5L), 0);
        assertEquals(5L, Numbers.ofLong("1234.5", 5L), 0);
    }

    /**
     * Test for {@link Numbers#ofShort(String)}.
     */
    @Test
    public void testToShortString() {
        assertEquals(12345, Numbers.ofShort("12345"), 0);
        assertEquals(0, Numbers.ofShort("abc"), 0);
        assertEquals(0, Numbers.ofShort(""), 0);
        assertEquals(0, Numbers.ofShort(null), 0);
    }

    /**
     * Test for {@link Numbers#ofShort(String, short)}.
     */
    @Test
    public void testToShortStringI() {
        assertEquals(12345, Numbers.ofShort("12345", (short) 5), 0);
        assertEquals(5, Numbers.ofShort("1234.5", (short) 5), 0);
    }
}