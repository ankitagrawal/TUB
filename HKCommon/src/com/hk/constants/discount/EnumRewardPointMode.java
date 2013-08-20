package com.hk.constants.discount;

import com.hk.domain.offer.rewardPoint.RewardPointMode;

import java.util.Arrays;
import java.util.List;

/**
 * Generated
 */
public enum EnumRewardPointMode {
    TPSL_ADJUSTMENTS(10L, "TPSL Adjustments"), HK_CASHBACK(20L, "HealthKart Cashback"), HK_ADJUSTMENTS(30L, "HealthKart Adjustments"), REFERRAL(40L, "Referral"), FB_SHARING(50L,
            "Facebook Sharing"), Prepay_Offer(60L, "Prepay"), HKPLUS_POINTS(70L, "HK Plus Points"), HKLOYALTY_POINTS(80L,"HK Loyalty Points"),
    HK_ORDER_CANCEL_POINTS(90L, "Order Cancellation Rewards Points");

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

    public static List<RewardPointMode> getCashBackModes(){
       return Arrays.asList(
                EnumRewardPointMode.HK_CASHBACK.asRewardPointMode(),
                EnumRewardPointMode.REFERRAL.asRewardPointMode(),
                EnumRewardPointMode.HKPLUS_POINTS.asRewardPointMode(),
                EnumRewardPointMode.HKLOYALTY_POINTS.asRewardPointMode()
        );
    }

    public RewardPointMode asRewardPointMode() {
        RewardPointMode rewardPointMode = new RewardPointMode();
        rewardPointMode.setId(this.getId());
        rewardPointMode.setName(this.getName());
        return rewardPointMode;
    }

}