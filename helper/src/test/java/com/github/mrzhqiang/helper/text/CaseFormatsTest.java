package com.github.mrzhqiang.helper.text;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaseFormatsTest {

    @Test
    public void ofCanonical() {
        String expected = "com-github-mrzhqiang-helper-namingstest.txt";
        String canonical = CaseFormats.ofCanonical(getClass(), ".txt");
        assertEquals(expected, canonical);
    }

    @Test
    public void ofSimple() {
        String expected = "namings-test.fxml";
        String simple = CaseFormats.ofSimple(getClass(), ".fxml");
        assertEquals(expected, simple);
    }

    @Test
    public void ofCamel() {
        String expected = "abb-acc-add";
        String camel = CaseFormats.ofCamel("AbbAccAdd");
        assertEquals(expected, camel);
    }
}