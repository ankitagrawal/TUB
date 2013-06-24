package com.hk.constants.inventory;


import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.rtv.ExtraInventoryLineItemType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Generated
 */
public enum EnumPurchaseOrderStatus {
	Generated(10L, "Generated"),
	SentForApproval(20L, "Sent For Approval"),
	Approved(30L, "Approved"),
	SentToSupplier(40L, "Sent To Supplier"),
	Received(50L, "Received"),
	Closed(100L, "Closed"),
	Cancelled(1000L, "Cancelled");

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

	// List contans all the status of PO ans sequence is decided by id
	public static List<PurchaseOrderStatus> geAllPurchaseOrderStatus() {

		List<PurchaseOrderStatus> PurchaseOrderStatusList = Arrays.asList(Generated.getPurchaseOrderStatus(), SentForApproval.getPurchaseOrderStatus(),
				Approved.getPurchaseOrderStatus(), SentToSupplier.getPurchaseOrderStatus(), Received.getPurchaseOrderStatus(),
				Closed.getPurchaseOrderStatus(), Cancelled.getPurchaseOrderStatus());
		Collections.sort(PurchaseOrderStatusList);
		return PurchaseOrderStatusList;


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

	public static List<PurchaseOrderStatus> getAllowedPOStatusToChange(PurchaseOrderStatus purchaseOrderStatus) {
		List<PurchaseOrderStatus> allowedPurchaseOrderStatusList = new ArrayList<PurchaseOrderStatus>();

		if (purchaseOrderStatus != null) {
			if (purchaseOrderStatus.equals(Generated.getPurchaseOrderStatus())) {
				allowedPurchaseOrderStatusList = Arrays.asList(Generated.getPurchaseOrderStatus(), SentForApproval.getPurchaseOrderStatus(), Cancelled.getPurchaseOrderStatus());
			} else if (purchaseOrderStatus.equals(SentForApproval.getPurchaseOrderStatus())) {
				allowedPurchaseOrderStatusList = Arrays.asList(Generated.getPurchaseOrderStatus(), Approved.getPurchaseOrderStatus(), Cancelled.getPurchaseOrderStatus());
			} else if (purchaseOrderStatus.equals(Approved.getPurchaseOrderStatus())) {
				allowedPurchaseOrderStatusList = Arrays.asList(Generated.getPurchaseOrderStatus(), SentToSupplier.getPurchaseOrderStatus(), Cancelled.getPurchaseOrderStatus());
			} else if (purchaseOrderStatus.equals(SentToSupplier.getPurchaseOrderStatus())) {
				allowedPurchaseOrderStatusList = Arrays.asList(Received.getPurchaseOrderStatus(), Cancelled.getPurchaseOrderStatus());
			} else if (purchaseOrderStatus.equals(Received.getPurchaseOrderStatus())) {
				allowedPurchaseOrderStatusList = Arrays.asList(Closed.getPurchaseOrderStatus());
			}
		}

		return allowedPurchaseOrderStatusList;
	}
	
	public PurchaseOrderStatus asEnumPurchaseOrderStatus(){
		PurchaseOrderStatus purchaseOrderStatus = new PurchaseOrderStatus();
		purchaseOrderStatus.setId(this.id);
		purchaseOrderStatus.setName(this.name);
		return purchaseOrderStatus;
	}
}