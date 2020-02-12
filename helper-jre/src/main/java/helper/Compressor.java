package helper;

import com.google.common.base.Preconditions;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;

/**
 * 压缩工具。
 *
 * @author mrzhqiang
 */
@Slf4j
public enum Compressor {
  ;

  private static final int BUFFER_SIZE = 8192;

  public static void zipCompress(String source, String destination) {
    Preconditions.checkNotNull(source, "source == null");
    Preconditions.checkNotNull(destination, "destination == null");

    Path sourcePath = Paths.get(source);
    if (Files.notExists(sourcePath)) {
      log.warn("ZIP 压缩源文件 {} 不存在！", sourcePath);
      return;
    }

    if (!destination.endsWith(".zip")) {
      destination = destination + ".zip";
    }
    Path destinationPath = Paths.get(destination);
    if (Files.exists(destinationPath)) {
      log.warn("ZIP 压缩目标文件 {} 已存在！", destinationPath);
      return;
    }
    InputOuts.mkdir(destinationPath);

    try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(destination))) {
      compress(sourcePath.toFile(), zos, "");
      log.info("完成 ZIP 压缩 {} 到 {}。", sourcePath, destinationPath);
    } catch (Exception e) {
      log.error("当 ZIP 压缩时，出现意料之外的错误！", e);
    }
  }

  private static void compress(File sourceFile, ZipOutputStream outputStream, String baseDir) {
    if (sourceFile.isDirectory()) {
      for (File file : Objects.requireNonNull(sourceFile.listFiles())) {
        compress(file, outputStream, baseDir + sourceFile.getName() + File.separator);
      }
    } else {
      compressFile(sourceFile, outputStream, baseDir);
    }
  }

  private static void compressFile(File sourceFile, ZipOutputStream outputStream, String baseDir) {
    if (sourceFile == null || !sourceFile.exists()) {
      return;
    }
    try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile))) {
      ZipEntry zipEntry = new ZipEntry(baseDir + sourceFile.getName());
      outputStream.putNextEntry(zipEntry);
      byte[] inbuf = new byte[BUFFER_SIZE];
      int n;
      while ((n = bis.read(inbuf)) != -1) {
        outputStream.write(inbuf, 0, n);
      }
    } catch (Exception e) {
      log.error("压缩文件" + sourceFile + "出错！", e);
    }
  }
}
