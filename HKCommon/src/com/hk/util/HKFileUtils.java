package com.hk.util;

import java.io.*;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipEntry;


public class HKFileUtils {
  public static File unzipFolder(String source, String destination) throws IOException {
    int buffer = 90000;
    byte data[] = new byte[buffer];
    File contentFolder = new File(destination);
    if (!contentFolder.exists()) {
      contentFolder.mkdir();
    }

    //get zip file content
    ZipInputStream zis = new ZipInputStream(new FileInputStream(source));
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
    zis.close();
    return contentFolder;
  }

  public static String fileToString(File file) {
    byte[] fileBytes = new byte[0];
    try {
      byte[] buffer = new byte[4096];
      ByteArrayOutputStream outs = new ByteArrayOutputStream();
      InputStream ins = new FileInputStream(file);

      int read = 0;
      while ((read = ins.read(buffer)) != -1) {
        outs.write(buffer, 0, read);
      }

      ins.close();
      outs.close();
      fileBytes = outs.toByteArray();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return new String(fileBytes);
  }
}
