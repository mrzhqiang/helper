package com.github.mrzhqiang.helper;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class StackTracesTest {

    @Test
    public void ofCurrent() {
        String firstTrace = StackTraces.ofCurrent();
        String secondTrace = StackTraces.ofCurrent();
        assertNotEquals(firstTrace, secondTrace);
    }
}
