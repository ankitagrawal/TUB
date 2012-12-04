package com.hk.constants.discount;

import com.hk.domain.offer.rewardPoint.RewardPointMode;

/**
 * Generated
 */
public enum EnumRewardPointMode {
	TPSL_ADJUSTMENTS(10L, "TPSL Adjustments"),
	HK_CASHBACK(20L, "HealthKart Cashback"),
	HK_ADJUSTMENTS(30L, "HealthKart Adjustments"),
	REFERRAL(40L, "Referral"),
	FB_SHARING(50L, "Facebook Sharing"),
	Prepay_Offer(60L, "Prepay"),
    HealthkartPlus(70L, "HealthkartPlus");

	private String name;
	private Long id;

	EnumRewardPointMode(Long id, String name) {
		this.name = name;
		this.id = id;
	}

    public RewardPointMode asRewardPointMode(){
        RewardPointMode rewardPointMode=new RewardPointMode();
        rewardPointMode.setId(this.id);
        rewardPointMode.setName(this.name);
        return rewardPointMode;
    }

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

}