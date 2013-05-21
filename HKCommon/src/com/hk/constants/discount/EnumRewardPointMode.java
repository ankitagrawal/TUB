package com.hk.constants.discount;

import com.hk.domain.offer.rewardPoint.RewardPointMode;

/**
 * Generated
 */
public enum EnumRewardPointMode {
    TPSL_ADJUSTMENTS(10L, "TPSL Adjustments"), HK_CASHBACK(20L, "HealthKart Cashback"), HK_ADJUSTMENTS(30L, "HealthKart Adjustments"), REFERRAL(40L, "Referral"), FB_SHARING(50L,
            "Facebook Sharing"), Prepay_Offer(60L, "Prepay"), HKPLUS_POINTS(70L, "HK Plus Points"), HKLOYALTY_POINTS(80L,"HK Loyalty Points");

    private String name;
    private Long   id;

    EnumRewardPointMode(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Long getId() {
        return this.id;
    }

    public RewardPointMode asRewardPointMode() {
        RewardPointMode rewardPointMode = new RewardPointMode();
        rewardPointMode.setId(this.getId());
        rewardPointMode.setName(this.getName());
        return rewardPointMode;
    }

}