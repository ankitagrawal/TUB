package com.akube.framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Author: Kani
 * Date: Aug 7, 2009
 */
public class ZipUtils {

  /**
   *
   * Example Directory Structure
   *
   * foo <br/>
   * |-foo1 <br/>
   * |-foo2 <br/>
   *   |-foofoo1 <br/>
   * bar <br/>
   * |-bar1 <br/>
   * |-bar2 <br/>
   *
   * <br/>
   *
   * Calling zipFolder("/foo", "foo.zip", "/temp")
   *
   * will create a foo.zip in the folder /temp. The zip will have the whole directory /foo zipped
   * up recursively
   *
   * @param rootPath  folder to zip
   * @param zipFileName name of zip file
   * @param zipDir path where zip will be placed
   * @return insignificant
   * @throws IOException
   */
  public static boolean zipFolder(String rootPath, String zipFileName, String zipDir) throws IOException {
    ZipOutputStream zos = null;
    try {
      //create a ZipOutputStream to zip the data to
      zos = new
          ZipOutputStream(new FileOutputStream(zipDir + "/" + zipFileName));
      zos.setLevel(0);
      //assuming that there is a directory named inFolder (If there
      //isn't create one) in the same directory as the one the code runs from,
      //call the addDirToZipStream method
      File rootPathFile = new File(rootPath);
      addToZipStream(rootPathFile, rootPathFile.getParent(), zos);
      //close the stream
    } catch (Exception e) {
      //handle exception
    } finally {
      if (zos != null) zos.close();
    }

    return true;
  }

  private static void addToZipStream(File zipLocation, String contextDirStr, ZipOutputStream zos) {
    try {
      if (zipLocation.isDirectory()) {
        //get a listing of the directory content
        String[] dirList = zipLocation.list();
        //loop through dirList, and zip the files
        for (int i = 0; i < dirList.length; i++) {
          File f = new File(zipLocation, dirList[i]);
          if (f.isDirectory()) {
            //if the File object is a directory, call this
            //function again to add its content recursively
            addToZipStream(f, contextDirStr, zos);
          } else {
            addFileToZipStream(f, contextDirStr, zos);
          }
        }
      } else {
        addFileToZipStream(zipLocation, contextDirStr, zos);
      }
    } catch (Exception e) {
      //handle exception
    }
  }

  private static void addFileToZipStream(File zipLocation, String contextDirStr, ZipOutputStream zos) {
    try {
      File contextDir = new File(contextDirStr);
      byte[] readBuffer = new byte[2156];
      int bytesIn = 0;
      //create a FileInputStream on top of f
      FileInputStream fileInputStream = new FileInputStream(zipLocation);
      //create a new zip entry

      ZipEntry zipEntry = new ZipEntry(getRelativePath(zipLocation.getParentFile(), contextDir) + "/" + zipLocation.getName());

      //place the zip entry in the ZipOutputStream object
      zos.putNextEntry(zipEntry);
      //now write the content of the file to the ZipOutputStream
      while ((bytesIn = fileInputStream.read(readBuffer)) != -1) {
        zos.write(readBuffer, 0, bytesIn);
      }
      //close the Stream
      fileInputStream.close();
    } catch (IOException e) {

    }
  }

  @SuppressWarnings("unchecked")
private static String getRelativePath(File file, File contextDir) throws IOException {
    String relativePath = "";
    file = new File(file + File.separator + "89243jmsjigs45u9w43545lkhj7").getParentFile();
    contextDir = new File(contextDir + File.separator + "984mvcxbsfgqoykj30487df556").getParentFile();
    ArrayList filePathStack = new ArrayList();
    int contextPathCount = 0;
    // build the path stack info to compare it afterwards
    file = file.getCanonicalFile();
    while (file != null) {
      filePathStack.add(0, file);
      file = file.getParentFile();
    }
    contextDir = contextDir.getCanonicalFile();
    while (contextDir != null) {
      contextPathCount++;
      contextDir = contextDir.getParentFile();
    }
    // compare as long it goes
    int count = contextPathCount - 1;
    while (count < filePathStack.size() - 1) {
      count++;
      file = (File) filePathStack.get(count);
      relativePath = relativePath + File.separator + file.getName();
    }
    return relativePath;
  }

}
