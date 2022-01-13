package com.github.mrzhqiang.helper.io;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * 资源管理器。
 *
 * @author mrzhqiang
 */
public final class Explorer {
    private Explorer() {
        // no instances
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Explorer.class);

    /**
     * 创建目录。
     * <p>
     * 注意：此方法会递归操作，即父路径不存在，也会创建父路径。
     * <p>
     * 创建操作只会在 path 不存在的情况下执行，已存在目录将不进行任何操作。
     *
     * @param path 路径。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是创建目录失败。
     */
    public static void mkdir(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("创建新目录：{}", path);
                }
            }
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("创建所有目录 [%s] 失败", path), e);
        }
    }

    /**
     * 创建文件。
     * <p>
     * 注意：如果开启日志的调试打印，则此方法将通过 org.slf4j.Logger 打印创建新文件的日志信息。
     * <p>
     * 创建操作只会在 path 不存在的情况下进行，已存在文件将不进行任何操作。
     *
     * @param path 路径。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是创建文件失败。
     */
    public static void create(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("创建新文件：{}", path);
                }
            }
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("创建失败 [%s]", path), e);
        }
    }

    /**
     * 删除目录或文件。
     * <p>
     * 注意：此方法将通过 org.slf4j.Logger 打印删除的日志，使用的是 info 级别。
     * <p>
     * 警告：如果是删除目录，则必须是一个空目录，一旦目录下存在文件，删除操作将失败，抛出相关异常。
     * <p>
     * 删除操作只会在 path 存在的情况下进行，已存在文件将只打印删除日志。
     *
     * @param path 路径。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是删除失败，另外也可能是删除的目录非空。
     */
    public static void delete(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            // 目录必须为空，否则将抛出 DirectoryNotEmptyException
            if (Files.exists(path)) {
                Files.delete(path);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("已删除：{}", path);
                }
            }
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("删除 [%s] 失败", path), e);
        }
    }

    /**
     * 删除所有目录或文件。
     * <p>
     * 注意：此方法内联 {@link #delete(Path)} 方法。
     * <p>
     * 通过 {@link Files#walkFileTree(Path, FileVisitor)} 遍历路径下的所有目录和文件。
     * <p>
     * todo walkFileTree 的 API 是深度优先遍历，但没有经过测试，需要看看是否从最深处开始调用 visitFile
     * todo 如果是这样的话，那么这个方法正常工作，否则需要另外寻找方法。
     *
     * @param path 路径。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是删除失败，另外也可能是删除的目录非空。
     */
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
            throw new ExplorerException(Strings.lenientFormat("删除 [%s] 失败", path), e);
        }
    }

    /**
     * 将指定内容写入到路径所代表的文件中。
     * <p>
     * 如果文件不存在，将自动创建。
     * <p>
     * 这个方法只会覆盖内容，不追加。
     *
     * @param path  文件路径。
     * @param bytes 文件内容的字节数组。
     * @throws NullPointerException 如果传入的 path 或 content 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是写入失败。
     */
    public static void write(Path path, byte[] bytes) {
        Preconditions.checkNotNull(path, "path == null");
        Preconditions.checkNotNull(bytes, "content == null");
        try {
            Files.write(path, bytes, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("无法写入到 [%s]", path), e);
        }
    }

    /**
     * 将指定内容追加到路径所代表的文件末尾。
     * <p>
     * 如果文件不存在，将自动创建。
     * <p>
     * 这个方法只会覆盖内容，不追加。
     *
     * @param path    文件路径。
     * @param content 文件内容。
     * @throws NullPointerException 如果传入的 path 或 content 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是写入失败。
     */
    public static void appendWrite(Path path, String content) {
        Preconditions.checkNotNull(path, "path == null");
        Preconditions.checkNotNull(content, "content == null");
        try {
            Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("无法写入到 [%s]", path), e);
        }
    }

    /**
     * 列出指定文件路径的所有内容。
     *
     * @param path 文件路径。
     * @return 字符串列表。列表中的每一个字符串元素代表文件的一行。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是读取失败。
     */
    public static List<String> lines(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            if (Files.exists(path)) {
                return Files.readAllLines(path);
            } else {
                return Collections.emptyList();
            }
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("无法读取文件内容 [%s]", path), e);
        }
    }

    /**
     * 列出指定目录路径下的子目录或子文件。
     * <p>
     * 需要使用 try-resources 语句关闭流。
     *
     * @param path 目录路径。
     * @return 路径流。仅包含当前目录下的子目录和子文件，不递归。
     * @throws NullPointerException 如果传入的 path 为 Null，则抛出此异常。
     * @throws ExplorerException    如果 IO 执行出现问题，则抛出此异常。通常情况下，是读取失败。
     */
    public static Stream<Path> list(Path path) {
        Preconditions.checkNotNull(path, "path == null");
        try {
            return Files.list(path);
        } catch (IOException e) {
            throw new ExplorerException(Strings.lenientFormat("无法列出目录内容 [%s]", path), e);
        }
    }
}
