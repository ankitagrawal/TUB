package com.hk.exception;

/**
 * User: kani
 * Time: 6 Nov, 2009 1:06:30 PM
 */
public abstract class HealthkartRuntimeException extends RuntimeException {
  protected HealthkartRuntimeException() {
  }

  protected HealthkartRuntimeException(String message) {
    super(message);
  }

  public HealthkartRuntimeException(Throwable e) {
    super(e);
  }
}
