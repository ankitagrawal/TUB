package com.hk.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class HKFileUtils {
  private static Logger logger = LoggerFactory.getLogger(HKFileUtils.class);
  private static final int buffer = 90000;

  public static File unzipFolder(String source, String destination) {
    byte data[] = new byte[buffer];
    File contentFolder = new File(destination);
    if (!contentFolder.exists()) {
      contentFolder.mkdir();
    }

    ZipInputStream zis = null;
    try {
      zis = new ZipInputStream(new FileInputStream(source));
      //get zipped file list entry
      ZipEntry ze = zis.getNextEntry();

      while (ze != null) {
        String fileName = ze.getName();
        File newFile = new File(destination + "/" + fileName);

        //checking is the zipped entry is a folder
        if (ze.isDirectory()) {
          (newFile).mkdir();
        } else {
          newFile.getParentFile().mkdirs();
          FileOutputStream fos = new FileOutputStream(newFile);
          int len;
          while ((len = zis.read(data)) > 0) {
            fos.write(data, 0, len);
          }
          fos.close();
        }
        ze = zis.getNextEntry();
      }
      zis.closeEntry();
      return contentFolder;
    } catch (IOException ioe) {
      logger.error("error unzipping folder: " + ioe);
    } finally {
      if (zis != null) {
        IOUtils.closeQuietly(zis);
      }
    }
    return null;
  }

  public static String fileToString(File file) {
    byte[] fileBytes;

    ByteArrayOutputStream outs = null;
    InputStream ins = null;
    try {
      byte[] data = new byte[buffer];
      outs = new ByteArrayOutputStream();
      ins = new FileInputStream(file);

      int read;
      while ((read = ins.read(data)) != -1) {
        outs.write(data, 0, read);
      }
      fileBytes = outs.toByteArray();
      return new String(fileBytes);
    } catch (IOException ioe) {
      logger.error("error converting file to string: " + file.getName() + ioe);
    } finally {
      IOUtils.closeQuietly(ins);
      IOUtils.closeQuietly(outs);
    }
    return null;
  }

  public static String getContentType(String fileUrl) {
    FileNameMap fileNameMap = URLConnection.getFileNameMap();
    return fileNameMap.getContentTypeFor(fileUrl);
  }

  public static String getPathAfterSubstring(String path, String substring) {
    String separator = File.separator;
    if (separator.equals("\\")) {
      return path.replaceAll("(.*\\\\" + substring + "\\\\)", "").replaceAll("\\\\", "/");
    } else if (separator.equals("/")) {
      return path.replaceAll("(.*/" + substring + "/)", "");
    } else {
      return null;
    }
  }

  public static String getInBetweenPath(String path, String substring1, String substring2) {
    String separator = File.separator;
    if (separator.equals("\\")) {
      return path.replaceAll("(.*\\\\" + substring1 + "\\\\)", "").replaceAll(substring2 + ".*", "").replaceAll("\\\\", "/");
    } else if (separator.equals("/")) {
      return path.replaceAll("(.*/" + substring1 + "/)", "").replaceAll(substring2 + ".*", "");
    } else {
      return null;
    }
  }
}
