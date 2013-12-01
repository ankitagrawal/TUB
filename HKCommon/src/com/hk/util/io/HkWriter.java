package com.hk.util.io;

import java.io.File;

/**
 * @author praveen.adlakha
 */
public interface HkWriter {

  /**
   * This will add header to a file
   * @param headerKey
   * @param headerValue
   * @return
   */
  public HkWriter addHeader(String headerKey, String headerValue);

  /**
   * Add a value fro column to row identified by rowNo, column values are written in order of insertion.Hopefully
   * this assumption should suffice all requireememntd or else we can extnd the design for random acesss.
   * @param rowNo
   * @return
   */
  public HkWriter addCell(int rowNo, Object value);

  /**
   * Write data to file identified by file path
   * @param filePath
   * @return
   */
  public File writeData(String filePath);
}
