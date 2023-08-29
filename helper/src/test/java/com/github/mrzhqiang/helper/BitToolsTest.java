package com.github.mrzhqiang.helper;

import com.github.mrzhqiang.helper.uppc.BitTools;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class BitToolsTest {

    @Test
    public void readShort() {
        byte[] normalArrays = {1, 2};
        int readShort = BitTools.readShort(normalArrays, 0);
        // 2 << 8 | 1 == 0000 0010 0000 0001 == 513
        assertEquals(513, readShort);

        byte[] negativeArrays = {-1, -1};
        readShort = BitTools.readShort(negativeArrays, 0);
        // -1 << 8 & 0xFF00 | -1 & 0x00FF == 1111 1111 1111 1111 == 65535
        assertEquals(65535, readShort);
    }

    @Test
    public void readString() {
        byte[] normalArrays = {48, 49, 50, 51};
        String readString = BitTools.readString(normalArrays, 0, 4);
        assertEquals("0123", readString);

        byte[] asciiArrays = {'A', 'B', 'C', 'd'};
        readString = BitTools.readString(asciiArrays, 0, 4);
        assertEquals("ABCd", readString);
    }

    @Test
    public void readMapleString() {
        byte[] mapleArrays = {3, 0, 'A', 'B', 'C', 'd'};
        String readMapleString = BitTools.readMapleString(mapleArrays, 0);
        assertEquals("ABC", readMapleString);
    }

    @Test
    public void rollLeft() {
        // 22 == byte == 0001 0110 >>> 1011 0000 == int >>> byte == -80
        // 注意 176 超出 byte 的取值范围 [-128,127]
        // 因此将 int 转为 byte 时，会变成 -80 == 1101 0000
        // 网上的文章众说纷纭，从现象来看，其实是 176 - 256 = -80
        byte rollLeft = BitTools.rollLeft((byte) 22, 3);
        // 所以通过转为无符号整数就可以还原
        assertEquals(-80, rollLeft);

        // -22 == byte >>> int == 1110 1010 >>> 0101 0111 == int == 87
        rollLeft = BitTools.rollLeft((byte) -22, 3);
        assertEquals(87, rollLeft);
    }

    @Test
    public void rollRight() {
        // 22 == byte == 0001 0110 >>> 1100 0010 == int >>> byte == -62
        byte rollRight = BitTools.rollRight((byte) 22, 3);
        assertEquals(-62, rollRight);

        // -22 == byte >>> int == 1110 1010 >>> 0101 1101 == int == 93
        rollRight = BitTools.rollRight((byte) -22, 3);
        assertEquals(93, rollRight);
    }

    @Test
    public void multiplyBytes() {
        byte[] normalArrays = {1, 2};
        byte[] actual = BitTools.multiplyBytes(normalArrays, normalArrays.length, 3);
        byte[] expected = new byte[normalArrays.length * 3];
        System.arraycopy(normalArrays, 0, expected, 0, normalArrays.length);
        System.arraycopy(normalArrays, 0, expected, normalArrays.length, normalArrays.length);
        System.arraycopy(normalArrays, 0, expected, normalArrays.length * 2, normalArrays.length);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void doubleToShortBits() {
        int pi = BitTools.doubleToShortBits(Math.PI);
        assertEquals(16393, pi);
    }
}