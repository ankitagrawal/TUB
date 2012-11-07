package com.hk.api.user;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public enum HkAPIUser {

    HEALTHKART_PLUS("hkplus");

    private String apiKey;

    private HkAPIUser(String apiKey) {
        this.apiKey = apiKey;
    }

    

    public String getApiKey() {
        return apiKey;
    }



    public static boolean containsApiKey(String apiKey) {
        for (HkAPIUser apiUser : HkAPIUser.values()) {
            if (apiUser.getApiKey().equals(apiKey)) {
                return true;
            }
        }
        return false;
    }

}
