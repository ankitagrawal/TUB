package com.hk.constants.rtv;

import com.hk.domain.inventory.rtv.ExtraInventoryLineItemType;

/**
 * 
 * @author Nihal
 *
 */

public enum EnumExtraInventoryLineItemType {

	Normal(10L, "Normal"), Short(20L, "Short");

	private String name;
	private Long id;

	EnumExtraInventoryLineItemType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
	
	public ExtraInventoryLineItemType asEnumExtraInventoryLineItemType(){
		ExtraInventoryLineItemType extraInventoryLineItemType = new ExtraInventoryLineItemType();
		extraInventoryLineItemType.setId(this.id);
		extraInventoryLineItemType.setName(this.name);
		return extraInventoryLineItemType;
	}
}
