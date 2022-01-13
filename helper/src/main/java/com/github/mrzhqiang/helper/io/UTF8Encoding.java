package com.github.mrzhqiang.helper.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * UTF-8 编码器。
 * <p>
 * 实现将指定文件转为 UTF-8 编码。
 */
public final class UTF8Encoding {
    private UTF8Encoding() {
        // no instance
    }

    /**
     * 转换指定路径为 UTF-8 编码格式。
     * <p>
     * 如果指定路径是一个目录，则递归执行其下的所有目录和文件。
     * <p>
     * 如果指定路径本身就是 UTF-8 编码，则只进行复制操作；否则进行转码操作。
     * <p>
     * todo 支持任意编码转为 UTF-8 编码。可惜的是，目前还没有合适的方法判断文件编码类型。
     * <p>
     * 注意：这个方法目前仅支持 GBK 编码转为 UTF-8 编码，现实生活中，此类应用场景也比较常见。
     *
     * @param source 指定路径。如果为 Null 或路径不存在，则不执行任何操作。
     * @param target 目标路径。如果为 NUll 则不执行任何操作。
     */
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
        } catch (Exception ignored) {
        }
    }

    /**
     * 检测文件是否为 UTF-8 编码。
     * <p>
     * 它实际上是检测文件是否支持 UTF-8 编码读取。
     *
     * @param path 路径。
     * @return True 文件支持使用 UTF-8 编码读取，即文件本身就是 UTF-8 编码。False，文件不是 UTF-8 编码，也不支持 UTF-8 读取。
     */
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
        try (Stream<Path> pathStream = Files.list(source)) {
            pathStream.forEach(path -> {
                Path newTarget = target.resolve(path.getFileName());
                if (Files.isDirectory(path)) {
                    try {
                        Files.createDirectories(newTarget);
                        converts(path, newTarget);
                    } catch (Exception ignored) {
                    }
                } else {
                    convert(path, newTarget);
                }
            });
        }
    }

    private static void writeUTF8(CharBuffer decode, Path target) {
        char[] chars = new char[decode.remaining()];
        decode.get(chars);
        String content = new String(chars);
        Explorer.write(target, content.getBytes(StandardCharsets.UTF_8));
    }
}
