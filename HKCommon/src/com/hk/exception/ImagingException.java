package com.hk.exception;

/**
 * User: rahul
 * Time: 30 Sep, 2009 3:08:54 PM
 */
@SuppressWarnings("serial")
public class ImagingException extends RuntimeException{

  private String filePath;

  public ImagingException(String message, String filePath) {
    super(message);
    this.filePath = filePath;
  }

  @Override public String toString() {
    return "ImagingException{" +
        "filePath=" + filePath +
        '}';
  }
}
