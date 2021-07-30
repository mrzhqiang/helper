package com.github.mrzhqiang.helper.io;

import org.junit.Assert;
import org.junit.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EncondingsTest {

    @Test
    public void testSupport() throws URISyntaxException {
        URL resource = this.getClass().getClassLoader().getResource("dirs/subdir/10200.js");
        Assert.assertTrue(UTF8Encoding.support(Paths.get(resource.toURI())));
    }

    @Test
    public void testConvertUTF8() throws URISyntaxException {
        Path source = Paths.get(this.getClass().getClassLoader().getResource("dirs").toURI());
        UTF8Encoding.convert(source, source.getParent().resolve("utf8"));
    }

}