package com.hk.constants.affiliate;

import com.hk.domain.affiliate.AffiliateStatus;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 8/20/12
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public enum EnumAffiliateStatus {

	Unverified(10L, "Unverified"),
	Verified(20L, "Verified"),
	Rejected(30L, "Rejected"),
	Cancelled(40L, "Cancelled");

	private String name;
	private Long id;

	EnumAffiliateStatus(Long id, String name) {
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public AffiliateStatus asAffiliateStatus() {
		AffiliateStatus affiliateStatus = new AffiliateStatus();
		affiliateStatus.setId(id);
		affiliateStatus.setName(name);
		return affiliateStatus;
	}

	public static List<EnumAffiliateStatus> getAllAffiliateStatus() {
		return Arrays.asList(
				EnumAffiliateStatus.Unverified,
				EnumAffiliateStatus.Verified,
				EnumAffiliateStatus.Cancelled,
				EnumAffiliateStatus.Rejected);
	}

}
