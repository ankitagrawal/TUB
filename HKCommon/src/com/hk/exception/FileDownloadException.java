package com.hk.exception;

/**
 * User: kani
 * Time: 6 May, 2010 12:21:10 PM
 */
@SuppressWarnings("serial")
public class FileDownloadException extends Exception {

  private int responseCode;

  public FileDownloadException(int responseCode) {
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }
}
