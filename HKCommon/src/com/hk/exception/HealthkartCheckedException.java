package com.hk.exception;

public class HealthkartCheckedException extends Exception {

  public HealthkartCheckedException(Throwable e) {
    super(e);
  }

  public HealthkartCheckedException(String message) {
    super(message);
  }

}
