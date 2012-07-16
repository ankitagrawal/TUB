package com.hk.constants.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.PaymentMode;




public enum EnumPaymentMode {
  CCAVENUE_DUMMY(1L, "CCAvenue Dummy"),
  TECHPROCESS_TEST(10L, "Tekprocess Test"),
  TECHPROCESS(15L, "Techprocess"),
  FREE_CHECKOUT(5L, "Free"),
  NEFT(20L, "NEFT"),
  ChequeDeposit(25L, "Cheque Deposit"),
  CashDeposit(30L, "Cash Deposit"),
  COD(40L, "COD"),
  COUNTER_CASH(50L, "Counter Cash"),
  CITRUS(60L, "Citrus NetBanking"),
  CITRUS_NetBanking_New(70L, "Citrus NetBanking"),
  CITRUS_CreditDebit(80L, "Citrus Credit Debit"),
  EBS(90L, "EBS Online Payment");

  private java.lang.String name;
  private java.lang.Long id;

  EnumPaymentMode(java.lang.Long id, java.lang.String name) {
    this.name = name;
    this.id = id;
  }

  public java.lang.String getName() {
    return name;
  }

  public java.lang.Long getId() {
    return id;
  }

  public PaymentMode asPaymenMode() {
    PaymentMode paymentMode = new PaymentMode();
    paymentMode.setId(id);
    paymentMode.setName(name);
    return paymentMode;
  }

  public static EnumPaymentMode getPaymentModeFromId(Long id) {
    for (EnumPaymentMode paymentMode : values()) {
      if (paymentMode.getId().equals(id)) return paymentMode;
    }
    return null;
  }

  public static List<EnumPaymentMode> getWorkingPaymentModes() {
    return Arrays.asList(EnumPaymentMode.CashDeposit,
        EnumPaymentMode.ChequeDeposit,
        EnumPaymentMode.CCAVENUE_DUMMY,
        EnumPaymentMode.COD,
        EnumPaymentMode.COUNTER_CASH,
        EnumPaymentMode.FREE_CHECKOUT,
        EnumPaymentMode.NEFT,
        EnumPaymentMode.TECHPROCESS,
        EnumPaymentMode.CITRUS);

  }

  public static List<Long> getPrePaidPaymentModes() {
    return Arrays.asList(
        EnumPaymentMode.CCAVENUE_DUMMY.getId(),
        EnumPaymentMode.TECHPROCESS.getId(),
        EnumPaymentMode.CITRUS.getId());

  }


  public static List<Long> getPaymentModeIDs(List<EnumPaymentMode> enumPaymentModes) {
    List<Long> paymenLModeIds = new ArrayList<Long>();
    for (EnumPaymentMode enumPaymentMode : enumPaymentModes) {
      paymenLModeIds.add(enumPaymentMode.getId());
    }
    return paymenLModeIds;
  }
}

