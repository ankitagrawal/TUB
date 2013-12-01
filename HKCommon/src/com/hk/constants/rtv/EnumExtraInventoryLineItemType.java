package com.hk.constants.rtv;

import com.hk.domain.inventory.rtv.ExtraInventoryLineItemType;

/**
 * 
 * @author Nihal
 *
 */

public enum EnumExtraInventoryLineItemType {

	Normal(10L, "Normal"), Short(20L, "Short"), RTV(30L, "RTV");

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
	
	public static EnumExtraInventoryLineItemType getById(Long id) {
	    for(EnumExtraInventoryLineItemType e : values()) {
	        if(e.id.equals(id)) return e;
	    }
	    return null;
	 }
}
