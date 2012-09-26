package com.hk.constants.payment;

import java.util.Arrays;
import java.util.List;


public enum EnumPaymentType {
	All(0L, "All"),
	PrePaid(1L, "PrePaid"),
	COD(2L, "COD");

	private String name;
	private Long id;

	EnumPaymentType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public static List<EnumPaymentType> getAllPaymentTypes() {
		return Arrays.asList(
				EnumPaymentType.All,
				EnumPaymentType.PrePaid,
				EnumPaymentType.COD);

	}

}