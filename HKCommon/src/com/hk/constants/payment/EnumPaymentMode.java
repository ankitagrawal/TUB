package com.hk.constants.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.PaymentMode;


public enum EnumPaymentMode {
    CCAVENUE_DUMMY(1L, "CCAvenue Dummy", 0.0D),
    TECHPROCESS_TEST(10L, "Tekprocess Test", 0.0D),
    TECHPROCESS(15L, "Techprocess", 0.021D),
    FREE_CHECKOUT(5L, "Free", 0.0D),
    NEFT(20L, "NEFT", 0.0D),
    ChequeDeposit(25L, "Cheque Deposit", 0.0D),
    CashDeposit(30L, "Cash Deposit", 0.0D),
    COD(40L, "COD", 0.0D),
    COUNTER_CASH(50L, "Counter Cash", 0.0D),
    CITRUS_NetBanking_Old(60L, "Citrus NetBanking Old", 0.017D),
    CITRUS_NetBanking_New(70L, "Citrus NetBanking", 0.017D),
    CITRUS_CreditDebit(80L, "Citrus Credit Debit", 0.0215),
    EBS(90L, "EBS Online Payment", 0.02D),
    SUBSCRIPTION_PAYMENT(95L, "Subscription Payment", 0.0D),
    PAYPAL_CreditDebit(100L, "Paypal Credit Debit", 0.0D);

    private java.lang.String name;
    private java.lang.Long id;
    private Double reconciliationCharges;

    EnumPaymentMode(java.lang.Long id, java.lang.String name, Double reconciliationCharges) {
        this.name = name;
        this.id = id;
        this.reconciliationCharges = reconciliationCharges;
    }

    public static EnumPaymentMode getPaymentMode(PaymentMode paymentMode) {
        if (paymentMode != null) {
            for (EnumPaymentMode enumPaymentMode : values()) {
                if (enumPaymentMode.getId().equals(paymentMode.getId())) {
                    return enumPaymentMode;
                }
            }
        }
        return null;
    }


    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

    public Double getReconciliationCharges() {
        return reconciliationCharges;
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
                EnumPaymentMode.CITRUS_NetBanking_Old,
                EnumPaymentMode.EBS,
                EnumPaymentMode.CITRUS_CreditDebit,
                EnumPaymentMode.SUBSCRIPTION_PAYMENT,
                EnumPaymentMode.CITRUS_NetBanking_New,
                EnumPaymentMode.PAYPAL_CreditDebit);

    }

    public static List<Long> getPrePaidPaymentModes() {
        return Arrays.asList(
                EnumPaymentMode.CCAVENUE_DUMMY.getId(),
                EnumPaymentMode.TECHPROCESS.getId(),
                EnumPaymentMode.EBS.getId(),
                EnumPaymentMode.CITRUS_CreditDebit.getId(),
                EnumPaymentMode.CITRUS_NetBanking_New.getId(),
                EnumPaymentMode.CITRUS_NetBanking_Old.getId(),
                EnumPaymentMode.SUBSCRIPTION_PAYMENT.getId());

    }


    public static List<Long> getPaymentModeIDs(List<EnumPaymentMode> enumPaymentModes) {
        List<Long> paymenLModeIds = new ArrayList<Long>();
        for (EnumPaymentMode enumPaymentMode : enumPaymentModes) {
            paymenLModeIds.add(enumPaymentMode.getId());
        }
        return paymenLModeIds;
    }
}

