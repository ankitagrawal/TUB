package com.hk.constants.inventory;

import java.util.Arrays;
import java.util.List;


public enum EnumInventoryBarcodeMapType {
	Active("Active"),
	Inactive("Inactive"),
	Deleted("Deleted");

	private String name;
	private Long id;

	EnumInventoryBarcodeMapType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<EnumInventoryBarcodeMapType> getAll() {
		return Arrays.asList(
				EnumInventoryBarcodeMapType.Active,
				EnumInventoryBarcodeMapType.Inactive,
				EnumInventoryBarcodeMapType.Deleted);

	}

}