package com.hk.constants.affiliate;

import com.hk.domain.affiliate.AffiliateTxnType;

public enum EnumAffiliateTxnType {
	PENDING(5L, "Pending"),
	ADD(10L, "Added"),
	PAYMENT_DUE(13L, "Payment Due"),
	PAID(18L, "Paid"),
	SENT(20L, "Sent"),
	ORDER_CANCELLED(30L, "Order Cancelled"),;

	private java.lang.String name;
	private java.lang.Long id;

	EnumAffiliateTxnType(Long id, java.lang.String name) {
		this.name = name;
		this.id = id;
	}

	public AffiliateTxnType asAffiliateTxnType() {
		AffiliateTxnType affiliateTxnType = new AffiliateTxnType();
		affiliateTxnType.setId(id);
		affiliateTxnType.setName(name);
		return affiliateTxnType;
	}


	public java.lang.String getName() {
		return name;
	}

	public java.lang.Long getId() {
		return id;
	}
}
