package com.hk.exception;

import net.sourceforge.stripes.action.RedirectResolution;

import com.hk.constants.url.URLConstants;

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
    REJECTED_BY_GATEWAY(25, "Rejected By Gateway"),
    REJECTED_BY_ISSUER(30, "Rejected By Issuer"),
    UNKNOWN(100,"Unknown"),
    BAD_ENQUIRY_CIT(35,"Bad enquiry be merchant"),
    INVALID_STATUS_CHANGE(40,"Invalid Payment Status Change"),
    MANDATORY_FIELD_MISSING(45,"Mandatory fields missing"),
    INVALID_REFUND_AMOUNT(50,"Requested Refund amount exceeds total amount(sale amount - sum(existing refund amounts))"),
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
