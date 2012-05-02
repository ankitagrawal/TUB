package com.hk.constants.marketing;


public class AnalyticsConstants {

  public static final boolean analytics = true;
  public static final String gaCode ="abc";

  static {
    /*analytics = ServiceLocatorFactory.getService(Key.get(Boolean.class, Names.named(Keys.Env.analytics)));
    gaCode = ServiceLocatorFactory.getService(Key.get(String.class, Names.named(Keys.Env.gaCode)));*/
      
      //TODO: rewrite
  }

}
