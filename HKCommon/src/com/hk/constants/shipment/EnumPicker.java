package com.hk.constants.shipment;

import java.util.Arrays;
import java.util.List;


public enum EnumPicker {
	A("A"),
	B("B");

	private String name;

	EnumPicker(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static List<EnumPacker> getAll() {
		return Arrays.asList(EnumPacker.A, EnumPacker.B);

	}

}