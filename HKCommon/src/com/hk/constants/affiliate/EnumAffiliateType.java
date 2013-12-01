package com.hk.constants.affiliate;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/20/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumAffiliateType {

	Individual(10L, "Individual"),
	Company(20L, "Company"),;

	private String name;
	private Long id;

	EnumAffiliateType(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public static List<EnumAffiliateType> getAllAffiliateTypes() {
		return Arrays.asList(
				EnumAffiliateType.Company,
				EnumAffiliateType.Individual);
	}

}
