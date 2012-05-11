package com.hk.constants.core;

/**
 * Generated
 */
public enum EnumEmailType {
    ReferralEmail(1L, "Referral Email"), CampaignEmail(10L, "Campaign Email"), NotifyUserEmail(20L, "Notify User Email"), MissYouEmail(30L, "Miss You Email"), CustomEmail(40L,
            "Custom Email");

    private java.lang.String name;
    private java.lang.Long   id;

    EnumEmailType(java.lang.Long id, java.lang.String name) {
        this.name = name;
        this.id = id;
    }

    public java.lang.String getName() {
        return name;
    }

    public java.lang.Long getId() {
        return id;
    }

}
