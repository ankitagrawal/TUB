package com.hk.web;

import java.util.Locale;

import net.sourceforge.stripes.action.LocalizableMessage;

/**
 * This is a standardized object being used throughout the app to return json responses.
 * it has a few fields to indicate the response status to gracefully handle errors, handling redirects etc.
 */
public class HealthkartResponse {

  public static final String STATUS_OK = "ok";
  public static final String STATUS_ERROR = "error";
  public static final String STATUS_ACCESS_DENIED = "denied";
  public static final String STATUS_RELOAD = "reload";
  public static final String STATUS_REDIRECT = "redirect";

  private String code;
  private String message;
  private Object data;

  public HealthkartResponse(String code, String message) {
    this.code = code;
    this.message = message;
  }

  public HealthkartResponse(String code, LocalizableMessage message, Locale locale) {
    this.code = code;
    this.message = message.getMessage(locale);
  }

  public HealthkartResponse(String code, LocalizableMessage message, Locale locale, Object data) {
    this.code = code;
    this.message = message.getMessage(locale);
    this.data = data;
  }

  public HealthkartResponse(String code, String message, Object data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

}
