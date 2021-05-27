package com.github.mrzhqiang.helper.io;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;

/**
 * 资源管理器。
 *
 * @author mrzhqiang
 */
public enum Explorer {
    ;

    private static final Logger LOGGER = LoggerFactory.getLogger("helper");

    public static void mkdir(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                LOGGER.info("创建新目录：{}", path);
            }
        } catch (IOException e) {
            throw new ExplorerException(String.format("创建所有目录 [%s] 失败", path), e);
        }
    }

    public static void create(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
                LOGGER.info("创建新文件：{}", path);
            }
        } catch (IOException e) {
            throw new ExplorerException(String.format("创建失败 [%s]", path), e);
        }
    }

    public static void delete(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            // 目录必须为空，否则将抛出 DirectoryNotEmptyException
            if (Files.exists(path)) {
                Files.delete(path);
            }
            LOGGER.info("已删除：{}", path);
        } catch (IOException e) {
            throw new ExplorerException(String.format("删除 [%s] 失败", path), e);
        }
    }

    public static void deleteAll(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.notExists(path)) {
                return;
            }
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    Explorer.delete(file);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new ExplorerException(String.format("删除 [%s] 失败", path), e);
        }
    }

    public static void write(Path path, String content) {
        Preconditions.checkNotNull(path, "path == null");
        Preconditions.checkNotNull(content, "content == null");
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ExplorerException(String.format("无法写入到 [%s]", path), e);
        }
    }

    public static void appendWrite(Path path, String content) {
        Preconditions.checkNotNull(path, "path == null");
        Preconditions.checkNotNull(content, "content == null");
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ExplorerException(String.format("无法写入到 [%s]", path), e);
        }
    }

    public static List<String> lines(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.exists(path)) {
                return Files.readAllLines(path);
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            throw new ExplorerException(String.format("无法读取文件内容 [%s]", path), e);
        }
    }
}
