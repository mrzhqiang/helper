package com.github.mrzhqiang.helper.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 压缩工具。
 *
 * @author mrzhqiang
 */
public final class Compressor {
    private Compressor() {
        // no instances
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Compressor.class);

    /**
     * 缓存大小。
     */
    private static final int BUFFER_SIZE = 8192;
    /**
     * 数字日期时间格式化。
     * <p>
     * 比如：0113134531
     */
    private static final DateTimeFormatter NUMBER_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("MMddHHmmss");

    /**
     * 使用 zip 方式压缩文件或目录。
     * <p>
     * 如果是源文件不存在，将只生成日志警告，不执行任何操作。
     * <p>
     * 如果目标文件已存在，将自动添加时间后缀。
     *
     * @param source      需要压缩的文件地址。
     * @param destination 压缩文件存放的地址。
     */
    public static void zipCompress(String source, String destination) {
        Preconditions.checkNotNull(source, "source == null");
        Preconditions.checkNotNull(destination, "destination == null");

        Path sourcePath = Paths.get(source);
        if (Files.notExists(sourcePath)) {
            LOGGER.warn("ZIP 压缩源文件 {} 不存在！", sourcePath);
            return;
        }

        String suffix = ".zip";
        boolean hasSuffix = destination.endsWith(suffix);
        String newDestination = hasSuffix ? destination : destination + suffix;
        Path destinationPath = Paths.get(newDestination);
        if (Files.exists(destinationPath)) {
            String numberDatetime = NUMBER_DATETIME_FORMATTER.format(LocalDateTime.now());
            newDestination = hasSuffix ? destination.replace(suffix, "") : destination;
            newDestination = newDestination + "_" + numberDatetime;
            destinationPath = Paths.get(newDestination + suffix);
            LOGGER.warn("ZIP 压缩目标文件 {} 已存在，已自动添加时间后缀", destinationPath);
        }
        Explorer.mkdir(destinationPath.getParent());
        Explorer.create(destinationPath);

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destinationPath.toFile()))) {
            compress(sourcePath, zos, "");
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("完成 ZIP 压缩 {} 到 {}。", sourcePath, destinationPath);
            }
        } catch (Exception e) {
            LOGGER.error("当 ZIP 压缩时，出现意料之外的错误！", e);
        }
    }

    private static void compress(Path sourcePath, ZipOutputStream outputStream, String baseDir) {
        if (Files.isDirectory(sourcePath)) {
            try (Stream<Path> pathStream = Explorer.list(sourcePath)) {
                pathStream.forEach(it -> {
                    String sourceFilename = sourcePath.getFileName().toString();
                    String subDir = Paths.get(baseDir, sourceFilename) + File.separator;
                    compress(it, outputStream, subDir);
                });
            }
        } else {
            compressFile(sourcePath, outputStream, baseDir);
        }
    }

    private static void compressFile(Path sourcePath, ZipOutputStream outputStream, String baseDir) {
        if (sourcePath == null || Files.notExists(sourcePath)) {
            return;
        }

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourcePath.toFile()))) {
            ZipEntry zipEntry = new ZipEntry(baseDir + sourcePath.getFileName());
            outputStream.putNextEntry(zipEntry);
            byte[] inbuf = new byte[BUFFER_SIZE];
            int n;
            while ((n = bis.read(inbuf)) != -1) {
                outputStream.write(inbuf, 0, n);
            }
        } catch (Exception e) {
            LOGGER.error(Strings.lenientFormat("压缩文件 %s 出错！", sourcePath), e);
        }
    }
}
