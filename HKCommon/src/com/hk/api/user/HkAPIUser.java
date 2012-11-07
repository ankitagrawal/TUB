package com.hk.api.user;

public enum HkAPIUser {

    HEALTHKART_PLUS("hkplus");

    private String appId;

    private HkAPIUser(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public static boolean containsAppId(String appId) {
        for (HkAPIUser apiUser : HkAPIUser.values()) {
            if (apiUser.getAppId().equals(appId)) {
                return true;
            }
        }
        return false;
    }

}
