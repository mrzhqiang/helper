package helper;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * 资源管理器。
 *
 * @author mrzhqiang
 */
@Slf4j
public enum Explorer {
  ;

  public static void mkdir(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      if (Files.notExists(path)) {
        Files.createDirectories(path);
        log.info("创建新目录：{}", path);
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
        log.info("创建新文件：{}", path);
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
      log.info("已删除：{}", path);
    } catch (IOException e) {
      throw new ExplorerException(String.format("删除 [%s] 失败", path), e);
    }
  }

  public static void deleteAll(Path path) {
    Preconditions.checkNotNull(path, "path == null");
    try {
      Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
        @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
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
