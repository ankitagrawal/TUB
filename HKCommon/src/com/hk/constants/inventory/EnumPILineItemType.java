package com.hk.constants.inventory;

import com.hk.domain.inventory.po.PiLineItemType;

public enum EnumPILineItemType {
	
	Normal(10L, "Normal"),
	Short(20L, "Short");

	private java.lang.String name;
	private java.lang.Long id;
	
	EnumPILineItemType(Long id, String name){
		this.name = name;
		this.id = id;
	}
	
	public java.lang.String getName() {
		return name;
	}

	public java.lang.Long getId() {
		return id;
	}
	
	public PiLineItemType asPiLineItemType(){
		PiLineItemType piLineItemType = new PiLineItemType();
		piLineItemType.setId(this.id);
		piLineItemType.setName(this.name);
		return piLineItemType;
		
	}
}
