package com.hk.exception;

public class HealthKartCouponException extends HealthkartCheckedException {
  public HealthKartCouponException(Throwable e) {
    super(e);
  }

  public HealthKartCouponException(String message) {
    super(message);
  }
}
