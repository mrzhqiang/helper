package com.github.mrzhqiang.helper.text;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author mrzhqiang
 */
public final class NamesTest {

    @Test
    public void firstLetter() {
        String defaultLetter = Names.firstLetter("");
        assertNotNull(defaultLetter);
        assertEquals("m", defaultLetter);

        String mrzhqiang = Names.firstLetter("play-home");
        assertNotNull(mrzhqiang);
        assertEquals("p", mrzhqiang);
    }

    @Test
    public void color() {
        String color = Names.color("mrzhqiang");
        // 0xff3d00
        assertEquals("0xff3d00", color);
    }

    @Test
    public void checkName() {
        assertTrue(Names.verify("中2"));
    }

    @Test
    public void checkNameAndLength() {
        assertTrue(Names.verify("中国No1", 2, 10));
    }

    @Test
    public void checkChinese() {
        assertFalse(Names.checkChinese("false"));
    }

    @Test
    public void checkChineseAndLength() {
        assertFalse(Names.checkChinese("中华人民共和国", 1, 4));
    }
}