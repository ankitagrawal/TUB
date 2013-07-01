package com.hk.constants.inventory;

import com.hk.domain.accounting.DebitNoteType;
import com.hk.domain.inventory.po.PurchaseOrderType;

public enum EnumPurchaseOrderType {

JIT(10L, "jit"), DROP_SHIP(20L, "drop ship"), REGULAR(30L, "regular");
	
	private String name;
	private Long id;

	EnumPurchaseOrderType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
	
	public PurchaseOrderType asEnumPurchaseOrderType(){
		PurchaseOrderType purchaseOrderType = new PurchaseOrderType();
		purchaseOrderType.setId(this.id);
		purchaseOrderType.setName(this.name);
		return purchaseOrderType;
	}
	
	public static EnumPurchaseOrderType getById(Long id) {
	    for(EnumPurchaseOrderType e : values()) {
	        if(e.id.equals(id)) return e;
	    }
	    return null;
	 }
}
