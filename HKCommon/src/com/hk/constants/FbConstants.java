package com.hk.constants;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

@Component
public class FbConstants {

    public static String fbFanAppId;

    public static String fbFanPageId;
    public static String apiKey;
    public static String apiSecret;
    public static String appId;
    public static String appUrl;

    public static String contestApiKey;
    public static String contestApiSecret;
    public static String contestAppId;
    public static String contestAppUrl;

    public static String promoApiKey;
    public static String promoApiSecret;
    public static String promoAppId;
    public static String promoAppUrl;

    public static String promo2ApiKey;
    public static String promo2ApiSecret;
    public static String promo2AppId;
    public static String promo2AppUrl;

    @Value("#{hkEnvProps['" + Keys.Env.fbFanAppId + "']}")
    public String        fbFanAppIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbFanPageId + "']}")
    public String        fbFanPageIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbApiKey + "']}")
    public String        apiKeyString;
    @Value("#{hkEnvProps['" + Keys.Env.fbApiSecret + "']}")
    public String        apiSecretString;
    @Value("#{hkEnvProps['" + Keys.Env.fbAppId + "']}")
    public String        appIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbAppUrl + "']}")
    public String        appUrlString;
    @Value("#{hkEnvProps['" + Keys.Env.fbContestApiKey + "']}")
    public String        contestApiKeyString;
    @Value("#{hkEnvProps['" + Keys.Env.fbContestApiSecret + "']}")
    public String        contestApiSecretString;
    @Value("#{hkEnvProps['" + Keys.Env.fbContestAppId + "']}")
    public String        contestAppIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbContestAppUrl + "']}")
    public String        contestAppUrlString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromoApiKey + "']}")
    public String        promoApiKeyString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromoApiSecret + "']}")
    public String        promoApiSecretString;
    @Value("#{hkEnvProps['" + Keys.Env.fbFanAppId + "']}")
    public String        promoAppIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromoAppUrl + "']}")
    public String        promoAppUrlString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromo2ApiKey + "']}")
    public String        promo2ApiKeyString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromo2ApiSecret + "']}")
    public String        promo2ApiSecretString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromo2AppId + "']}")
    public String        promo2AppIdString;
    @Value("#{hkEnvProps['" + Keys.Env.fbPromo2AppUrl + "']}")
    public String        promo2AppUrlString;

    @PostConstruct
    public void postConstruction() {
        fbFanAppId = this.fbFanAppIdString;
        fbFanPageId = this.fbFanPageIdString;
        apiKey = this.apiKeyString;
        apiSecret = this.apiSecretString;
        appId = this.appIdString;
        appUrl = this.appUrlString;

        contestApiKey = this.contestApiKeyString;
        contestApiSecret = this.contestApiSecretString;
        contestAppId = this.contestAppIdString;
        contestAppUrl = this.contestAppUrlString;

        promoApiKey = this.promoApiKeyString;
        promoApiSecret = this.promoApiSecretString;
        promoAppId = this.promoAppIdString;
        promoAppUrl = this.promoAppUrlString;

        promo2ApiKey = this.promo2ApiKeyString;
        promo2ApiSecret = this.promo2ApiSecretString;
        promo2AppId = this.promo2AppIdString;
        promo2AppUrl = this.promo2AppUrlString;
    }

    public static class Session {
        public static String fbUserClient  = "fbUserClient";
        public static String fbQueryString = "fbQueryString";
    }

    public static class Param {
        public static String fb_sig         = "fb_sig";
        public static String signed_request = "signed_request";

        // for params in the session cookie
        public static String access_token   = "access_token";
        public static String expires        = "expires";
        public static String secret         = "secret";
        public static String session_key    = "session_key";
        public static String sig            = "sig";
        public static String uid            = "uid";

    }

}
