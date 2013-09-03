package com.hk.api.hkedge.response;


public abstract class AbstractApiBaseResponse{

  protected boolean exception = false;
  protected String message;

  public boolean isException() {
    return exception;
  }

  public void setException(boolean exception) {
    this.exception = exception;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
