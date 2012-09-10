package com.hk.constants.inventory;


import com.hk.domain.core.PurchaseOrderStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Generated
 */
public enum EnumPurchaseOrderStatus {
	Generated(10L, "Generated"),
	SentForApproval(20L, "Sent For Approval"),
	Approved(30L, "Approved"),
	SentToSupplier(40L, "Sent To Supplier"),
	Cancelled(100L, "Cancelled"),
	Deleted(1000L, "Deleted"),;

	private String name;
	private Long id;

	EnumPurchaseOrderStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public PurchaseOrderStatus getPurchaseOrderStatus() {
		PurchaseOrderStatus purchaseOrderStatus = new PurchaseOrderStatus();
		purchaseOrderStatus.setId(this.id);
		purchaseOrderStatus.setName(this.name);
		return purchaseOrderStatus;
	}

	public static List<PurchaseOrderStatus> getStatusForNonApprover() {
		return Arrays.asList(Generated.getPurchaseOrderStatus(), SentForApproval.getPurchaseOrderStatus(), SentToSupplier.getPurchaseOrderStatus());
	}

	public static List<PurchaseOrderStatus> geAllPurchaseOrderStatus() {
		return Arrays.asList(Generated.getPurchaseOrderStatus(), SentForApproval.getPurchaseOrderStatus(),
				Approved.getPurchaseOrderStatus(), SentToSupplier.getPurchaseOrderStatus(),
				Cancelled.getPurchaseOrderStatus(), Deleted.getPurchaseOrderStatus());
	}

	public static PurchaseOrderStatus getNextPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus) {
		List<PurchaseOrderStatus> purchaseOrderStatusList = geAllPurchaseOrderStatus();
		for (int i = 0; i < purchaseOrderStatusList.size(); i++) {
			if (purchaseOrderStatusList.get(i).equals(purchaseOrderStatus)) {
				return purchaseOrderStatusList.get(i + 1);
			}

		}
		return null;
	}
}