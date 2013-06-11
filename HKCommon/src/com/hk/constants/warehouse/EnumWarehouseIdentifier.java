package com.hk.constants.warehouse;

public enum EnumWarehouseIdentifier {
	
	GGN_Bright_Warehouse("HARYANA", "GGN Bright Warehouse"), 
	MUM_Bright_Warehouse("MUMBAI", "MUM Bright Warehouse"); 

	private String state;
	private String name;
	
	EnumWarehouseIdentifier(String state, String name) {
		this.state = state;
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
