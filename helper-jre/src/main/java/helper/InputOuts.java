package helper;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * IO 工具。
 *
 * @author mrzhqiang
 */
@Slf4j
public enum InputOuts {
  ;

  public static void mkdir(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      Files.createDirectories(path);
      log.info("创建新目录：{}", path);
    } catch (IOException e) {
      throw new InputOutException(String.format("创建所有目录 [%s] 失败", path), e);
    }
  }

  public static void create(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      Files.createFile(path);
      log.info("创建新文件：{}", path);
    } catch (IOException e) {
      throw new InputOutException(String.format("创建失败 [%s]", path), e);
    }
  }

  public static void delete(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      // 目录必须为空，否则将抛出 DirectoryNotEmptyException
      Files.delete(path);
      log.info("已删除：{}", path);
    } catch (IOException e) {
      throw new InputOutException(String.format("删除 [%s] 失败", path), e);
    }
  }

  public static void deleteAll(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
          Files.delete(file);
          log.info("已删除：{}", file);
          return FileVisitResult.CONTINUE;
        }
      });
    } catch (IOException e) {
      throw new InputOutException(String.format("删除 [%s] 失败", path), e);
    }
  }

  public static void write(Path path, String content) {
    Preconditions.checkNotNull(path, "path == null");
    Preconditions.checkNotNull(content, "content == null");
    try {
      Files.write(path, content.getBytes(), StandardOpenOption.CREATE);
    } catch (IOException e) {
      throw new InputOutException(String.format("无法写入到 [%s]", path), e);
    }
  }

  public static void appendWrite(Path path, String content) {
    Preconditions.checkNotNull(path, "path == null");
    Preconditions.checkNotNull(content, "content == null");
    try {
      Files.write(path, content.getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new InputOutException(String.format("无法写入到 [%s]", path), e);
    }
  }

  public static List<String> lines(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      return Files.readAllLines(path);
    } catch (IOException e) {
      throw new InputOutException(String.format("无法读取文件内容 [%s]", path), e);
    }
  }
}
