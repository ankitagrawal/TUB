package com.hk.constants;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.hk.constants.core.Keys;

@Component
public class FbConstants {


	@Value("#{hkEnvProps['" + Keys.Env.fbFanAppId + "']}")
  public static String fbFanAppId;

	@Value("#{hkEnvProps['" + Keys.Env.fbFanPageId + "']}")
  public static  String fbFanPageId="";
	@Value("#{hkEnvProps['" + Keys.Env.fbApiKey + "']}")
  public static  String apiKey="";
	@Value("#{hkEnvProps['" + Keys.Env.fbApiSecret + "']}")
  public static  String apiSecret="";
	@Value("#{hkEnvProps['" + Keys.Env.fbAppId + "']}")
  public static  String appId="";
	@Value("#{hkEnvProps['" + Keys.Env.fbAppUrl + "']}")
  public static  String appUrl="";

	@Value("#{hkEnvProps['" + Keys.Env.fbContestApiKey + "']}")
  public static  String contestApiKey="";
	@Value("#{hkEnvProps['" + Keys.Env.fbContestApiSecret + "']}")
  public static  String contestApiSecret="";
	@Value("#{hkEnvProps['" + Keys.Env.fbContestAppId + "']}")
  public static  String contestAppId="";
	@Value("#{hkEnvProps['" + Keys.Env.fbContestAppUrl + "']}")
  public static  String contestAppUrl="";

	@Value("#{hkEnvProps['" + Keys.Env.fbPromoApiKey + "']}")
  public static  String promoApiKey="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromoApiSecret + "']}")
  public static  String promoApiSecret="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromoAppId + "']}")
  public static  String promoAppId="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromoAppUrl + "']}")
  public static  String promoAppUrl="";

	@Value("#{hkEnvProps['" + Keys.Env.fbPromo2ApiKey + "']}")
  public static  String promo2ApiKey="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromo2ApiSecret + "']}")
  public static  String promo2ApiSecret="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromo2AppId + "']}")
  public static  String promo2AppId="";
	@Value("#{hkEnvProps['" + Keys.Env.fbPromo2AppUrl + "']}")
  public static  String promo2AppUrl ="";

  public static class Session {
    public static  String fbUserClient = "fbUserClient";
    public static  String fbQueryString = "fbQueryString";
  }

  public static class Param {
    public static  String fb_sig = "fb_sig";
    public static  String signed_request = "signed_request";

    // for params in the session cookie
    public static  String access_token = "access_token";
    public static  String expires = "expires";
    public static  String secret = "secret";
    public static  String session_key = "session_key";
    public static  String sig = "sig";
    public static  String uid = "uid";

  }

}
