package com.hk.constants.catalog.image;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 9/10/12
 * Time: 12:24 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumImageType {

	FrontFacingEye(5L, "FrontFacingEye"),
	SideFacingEye(7L, "SideFacingEye"),
	SupplementsInfo(10L, "SupplementsInfo");

	private String name;
	private Long id;

	EnumImageType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}
	public static List<EnumImageType> getAllImageTypes() {
		return Arrays.asList(
				EnumImageType.FrontFacingEye,
				EnumImageType.SideFacingEye,
				EnumImageType.SupplementsInfo
				);

	}

}
