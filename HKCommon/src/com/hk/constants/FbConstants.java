package com.hk.constants;


public class FbConstants {

    
    //TODO: rewrite
    
  /*public static final String fbFanAppId;
  
  public static final String fbFanPageId;

  public static final String apiKey;
  public static final String apiSecret;
  public static final String appId;
  public static final String appUrl;

  public static final String contestApiKey;
  public static final String contestApiSecret;
  public static final String contestAppId;
  public static final String contestAppUrl;

  public static final String promoApiKey;
  public static final String promoApiSecret;
  public static final String promoAppId;
  public static final String promoAppUrl;

  public static final String promo2ApiKey;
  public static final String promo2ApiSecret;
  public static final String promo2AppId;
  public static final String promo2AppUrl;
*/
  static {
  /*  fbFanAppId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbFanAppId)));
    fbFanPageId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbFanPageId)));

    apiKey = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbApiKey)));
    apiSecret = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbApiSecret)));
    appId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbAppId)));
    appUrl = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbAppUrl)));

    contestApiKey = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbContestApiKey)));
    contestApiSecret = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbContestApiSecret)));
    contestAppId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbContestAppId)));
    contestAppUrl = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbContestAppUrl)));

    promoApiKey = ServiceLocatorFactory.getService(Key.get(String.class, Names.named   (Keys.Env.fbPromoApiKey)));
    promoApiSecret = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbPromoApiSecret)));
    promoAppId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named    (Keys.Env.fbPromoAppId)));
    promoAppUrl = ServiceLocatorFactory.getService(Key.get(String.class, Names.named   (Keys.Env.fbPromoAppUrl)));

    promo2ApiKey = ServiceLocatorFactory.getService(Key.get(String.class, Names.named   (Keys.Env.fbPromo2ApiKey)));
    promo2ApiSecret = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.fbPromo2ApiSecret)));
    promo2AppId = ServiceLocatorFactory.getService(Key.get(String.class, Names.named    (Keys.Env.fbPromo2AppId)));
    promo2AppUrl = ServiceLocatorFactory.getService(Key.get(String.class, Names.named   (Keys.Env.fbPromo2AppUrl)));
*/  
      //TODO: rewrite
      }

  public static class Session {
    public static final String fbUserClient = "fbUserClient";
    public static final String fbQueryString = "fbQueryString";
  }

  public static class Param {
    public static final String fb_sig = "fb_sig";
    public static final String signed_request = "signed_request";

    // for params in the session cookie
    public static final String access_token = "access_token";
    public static final String expires = "expires";
    public static final String secret = "secret";
    public static final String session_key = "session_key";
    public static final String sig = "sig";
    public static final String uid = "uid";

  }

}
