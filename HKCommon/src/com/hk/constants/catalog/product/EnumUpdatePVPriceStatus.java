package com.hk.constants.catalog.product;

import java.util.Arrays;
import java.util.List;


public enum EnumUpdatePVPriceStatus {
	Pending(10L, "Pending"),
	Updated(20L, "Updated"),
	Ignored(30L, "Ignored");

	private String name;
	private Long id;

	EnumUpdatePVPriceStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public static List<EnumUpdatePVPriceStatus> getAllStatuses() {
		return Arrays.asList(EnumUpdatePVPriceStatus.Pending, EnumUpdatePVPriceStatus.Updated, EnumUpdatePVPriceStatus.Ignored);

	}

}