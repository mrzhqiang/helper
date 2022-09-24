package com.github.mrzhqiang.helper;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class ExceptionsTest {

    @Test
    public void of() {
        String runtime = Exceptions.ofTrace(new RuntimeException("runtime"));
        String nullPointer = Exceptions.ofTrace(new NullPointerException("null"));
        assertNotEquals(runtime, nullPointer);
    }
}
