package com.hk.constants.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.PaymentStatus;


/**
 * Generated
 */
public enum EnumPaymentStatus {

  REQUEST(1L, "Requested"),
  AUTHORIZATION_PENDING(2L, "Authorization Pending"),
  SUCCESS(3L, "Success"),
  FAILURE(4L, "Failure"),
  ERROR(5L, "Error"),
  CANCELLED_OR_REFUNDED(6L, "Cancelled/Refunded"),
  ON_DELIVERY(7L, "On delivery"),
  ;

  private java.lang.String name;
  private java.lang.Long id;

  EnumPaymentStatus(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public PaymentStatus asPaymenStatus() {
    PaymentStatus paymentStatus = new PaymentStatus();
    paymentStatus.setId(id);
    paymentStatus.setName(name);
    return paymentStatus;
  }

  public static List<Long> getEnumIDs(List<EnumPaymentStatus> enumList) {
    List<Long> getEnumIDs = new ArrayList<Long>();
    for (EnumPaymentStatus enumPaymentStatus : enumList) {
      getEnumIDs.add(enumPaymentStatus.getId());
    }
    return getEnumIDs;
  }

  public static List<Long> getEscalablePaymentStatusIds() {
    return getEnumIDs(Arrays.asList(EnumPaymentStatus.SUCCESS, EnumPaymentStatus.ON_DELIVERY));
  }

    public static List<Long> getAuthPendingPaymentStatusIds() {
        return getEnumIDs(Arrays.asList(EnumPaymentStatus.AUTHORIZATION_PENDING, EnumPaymentStatus.ERROR, EnumPaymentStatus.REQUEST));
    }

    public static List<PaymentStatus> getEscalablePaymentStatuses() {
        return Arrays.asList(EnumPaymentStatus.SUCCESS.asPaymenStatus(), EnumPaymentStatus.ON_DELIVERY.asPaymenStatus());
    }

    public static List<PaymentStatus> getSeekPaymentStatuses() {
        return Arrays.asList(REQUEST.asPaymenStatus(),
                AUTHORIZATION_PENDING.asPaymenStatus(),
                FAILURE.asPaymenStatus(),
                ERROR.asPaymenStatus()
        );
    }

    public static List<Long> getOnlinePaymentErrorStatusIds() {
        return Arrays.asList(EnumPaymentStatus.REQUEST.getId(),
                EnumPaymentStatus.ERROR.getId(),
                EnumPaymentStatus.AUTHORIZATION_PENDING.getId());
    }

  public static List<Long> getPaymentSuccessPageStatusIds() {
      return getEnumIDs(Arrays.asList(EnumPaymentStatus.SUCCESS,EnumPaymentStatus.ON_DELIVERY, EnumPaymentStatus.AUTHORIZATION_PENDING));
  }

}

