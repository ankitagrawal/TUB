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
public enum EnumAffiliateMode {

	Online(10L, "Online"),
	Offline(20L, "Offline"),
	Both(30L, "Both");

	private String name;
	private Long id;

	EnumAffiliateMode(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public static List<EnumAffiliateMode> getAllAffiliateModes() {
		return Arrays.asList(
				EnumAffiliateMode.Offline,
				EnumAffiliateMode.Online,
				EnumAffiliateMode.Both);
	}

}
