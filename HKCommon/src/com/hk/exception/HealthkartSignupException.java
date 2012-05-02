package com.hk.exception;

public class HealthkartSignupException extends HealthkartCheckedException{
  public HealthkartSignupException(Throwable e) {
    super(e);
  }

  public HealthkartSignupException(String message) {
    super(message);
  }
}
