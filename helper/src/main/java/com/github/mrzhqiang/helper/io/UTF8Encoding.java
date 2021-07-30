package com.github.mrzhqiang.helper.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public enum UTF8Encoding {
    ;

    public static void convert(Path source, Path target) {
        if (source == null || target == null || Files.notExists(source)) {
            return;
        }

        try {
            if (Files.isDirectory(source)) {
                Files.createDirectories(target);
                converts(source, target);
                return;
            }

            if (UTF8Encoding.support(source)) {
                Files.copy(source, target);
                return;
            }

            CharBuffer decode = Charset.forName("GBK").decode(ByteBuffer.wrap(Files.readAllBytes(source)));
            writeUTF8(decode, target);
        } catch (Exception ignore) {
        }
    }

    public static boolean support(Path path) {
        if (path == null || Files.notExists(path)) {
            return false;
        }

        try {
            CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder();
            decoder.reset();

            byte[] bytes = Files.readAllBytes(path);
            decoder.decode(ByteBuffer.wrap(bytes));
        } catch (Exception ignore) {
            return false;
        }
        return true;
    }

    private static void converts(Path source, Path target) throws IOException {
        Files.list(source).forEach(path -> {
            Path newTarget = target.resolve(path.getFileName());
            if (Files.isDirectory(path)) {
                try {
                    Files.createDirectories(newTarget);
                    converts(path, newTarget);
                } catch (Exception ignore) {
                }
            } else {
                convert(path, newTarget);
            }
        });
    }

    private static void writeUTF8(CharBuffer decode, Path target) throws IOException {
        char[] chars = new char[decode.remaining()];
        decode.get(chars);
        String content = new String(chars);
        Files.write(target, content.getBytes(StandardCharsets.UTF_8));
    }
}
