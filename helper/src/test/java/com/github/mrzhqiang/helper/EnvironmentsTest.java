package com.github.mrzhqiang.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EnvironmentsTest {

    @Test
    public void debug() {
        String property = System.getProperty("intellij.debug.agent");
        boolean debug = Environments.debug();
        assertEquals(Boolean.parseBoolean(property), debug);
    }
}