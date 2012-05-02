package com.hk.constants.inventory;

import com.hk.domain.inventory.po.PurchaseInvoiceStatus;


public enum EnumPurchaseInvoiceStatus {
	PurchaseInvoiceGenerated(10L, "Purchase Invoice Generated"),;
	private String name;
	private Long id;

	EnumPurchaseInvoiceStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public PurchaseInvoiceStatus asPurchaseInvoiceStatus() {
		PurchaseInvoiceStatus purchaseInvoiceStatus = new PurchaseInvoiceStatus();
		purchaseInvoiceStatus.setId(this.getId());
		purchaseInvoiceStatus.setName(this.getName());
		return purchaseInvoiceStatus;
	}

}
