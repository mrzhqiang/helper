package com.github.mrzhqiang.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class ExceptionsTest {

    @Test
    public void ofMessage() {
        String runtime = Exceptions.ofMessage(new RuntimeException("runtime"));
        assertEquals("runtime", runtime);
    }

    @Test
    public void ofTrace() {
        String runtime = Exceptions.ofTrace(new RuntimeException("runtime"));
        String nullPointer = Exceptions.ofTrace(new NullPointerException("null"));
        assertNotEquals(runtime, nullPointer);
    }
}
