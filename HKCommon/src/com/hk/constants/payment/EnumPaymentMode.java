package com.hk.constants.payment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hk.domain.core.PaymentMode;


public enum EnumPaymentMode {
    ONLINE_PAYMENT(1000L, "Online Payment"),
	FREE_CHECKOUT(5L, "Free"),
	NEFT(20L, "NEFT"),
	ChequeDeposit(25L, "Cheque Deposit"),
	CashDeposit(30L, "Cash Deposit"),
	COD(40L, "COD"),
	COUNTER_CASH(50L, "Counter Cash"),
	SUBSCRIPTION_PAYMENT(95L, "Subscription Payment"),
	OFFLINE_CARD_PAYMENT(100L, "Offline Credit/Debit Card");

	private java.lang.String name;
	private java.lang.Long id;
	private Double reconciliationCharges;

	EnumPaymentMode(java.lang.Long id, java.lang.String name) {
		this.name = name;
		this.id = id;
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
				EnumPaymentMode.ONLINE_PAYMENT,
				EnumPaymentMode.COD,
				EnumPaymentMode.COUNTER_CASH,
				EnumPaymentMode.FREE_CHECKOUT,
				EnumPaymentMode.NEFT,
                EnumPaymentMode.SUBSCRIPTION_PAYMENT);
	}

    public static List<Long> getAuthorizationPendingPaymentModes() {
        return Arrays.asList(
                EnumPaymentMode.CashDeposit.getId(),
                EnumPaymentMode.ChequeDeposit.getId(),
                EnumPaymentMode.COUNTER_CASH.getId(),
                EnumPaymentMode.NEFT.getId()
        );
    }

	public static List<Long> getPrePaidPaymentModes() {
		return Arrays.asList(
                EnumPaymentMode.SUBSCRIPTION_PAYMENT.getId(),
                EnumPaymentMode.FREE_CHECKOUT.getId(),
                EnumPaymentMode.ONLINE_PAYMENT.getId());
	}


	public static List<Long> getPaymentModeIDs(List<EnumPaymentMode> enumPaymentModes) {
		List<Long> paymenLModeIds = new ArrayList<Long>();
		for (EnumPaymentMode enumPaymentMode : enumPaymentModes) {
			paymenLModeIds.add(enumPaymentMode.getId());
		}
		return paymenLModeIds;
	}

    public static List<Long> getReconciliationModeIds() {
        return Arrays.asList(
                EnumPaymentMode.ONLINE_PAYMENT.getId(),
                EnumPaymentMode.COD.getId());
    }
}

