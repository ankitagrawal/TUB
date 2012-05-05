package com.hk.exception;

import com.hk.constants.url.URLConstants;

import net.sourceforge.stripes.action.RedirectResolution;

@SuppressWarnings("serial")
public class HealthkartPaymentGatewayException extends Exception {
  public static enum Error {
    AMOUNT_MISMATCH(1,"Amount Mismatch"),
    CHECKSUM_MISMATCH(2,"Checksum Mismatch"),
    DOUBLE_PAYMENT(3,"Double Payment"),
    INVALID_RESPONSE(4,"Invalid Response"),
    PAYMENT_NOT_FOUND(5,"Payment Not Found"),
    COD_CONTACT_INFO_INVALID(10, "Contact Information (name/phone) not provided"),
    KEY_NOT_FOUND(20, "KEY NOT FOUND"),
    UNKNOWN(100,"Unknown"),
    ;

    private int code;
    private String message;

    Error(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public String getMessage() {
      return message;
    }

    public static Error getErrorFromCode(int code) {
      for (Error error : Error.values()) {
        if (error.code == code) return error;
      }
      return UNKNOWN;
    }
  }

  private Error error;

  public HealthkartPaymentGatewayException() {
    super("Error in payment process");
  }

  public HealthkartPaymentGatewayException(Error error) {
    super(error.message);
    this.error = error;
  }

  public RedirectResolution getRedirectResolution() {
    return new RedirectResolution(URLConstants.PAYMENT_ERROR_LINK).addParameter("errorCode", error.getCode());
  }

  public Error getError() {
    return error;
  }

}
