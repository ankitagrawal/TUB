package com.hk.constants.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.PaymentMode;
import com.hk.constants.payment.EnumPaymentType;


public enum EnumAuditStatus {
  Pending(0L, "Pending"),
  Done(1L, "Done"),
  Invalid(99L, "Invalid"),
  Deleted(999L, "Deleted")
	;

  private String name;
  private Long id;

  EnumAuditStatus(Long id, String name) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public Long getId() {
    return id;
  }

	public static List<EnumAuditStatus> getAllList() {
    return Arrays.asList(
        EnumAuditStatus.Pending,
        EnumAuditStatus.Done);

  }

}