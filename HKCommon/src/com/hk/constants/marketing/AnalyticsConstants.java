package com.hk.constants.marketing;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

@Component
public class AnalyticsConstants {

  public static String gaCode;

  public static boolean analytics;

  @Value("#{hkEnvProps['" + Keys.Env.analytics + "']}")
  private String analyticsString;

  @Value("#{hkEnvProps['" + Keys.Env.gaCode + "']}")
  private String analyticsCode;

  @PostConstruct
  public void postConstruction() {
    // String anaylticsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.analytics);

    analytics = StringUtils.isNotBlank(analyticsString) && Boolean.parseBoolean(analyticsString);

    gaCode = analyticsCode;

  }

  public static enum CustomVarSlot {
    slot1(1, "slot1name", CustomVarScope.pageLevel),
    firstPurchaseDate(2, "FirstPurchaseDate", CustomVarScope.visitorLevel),
    signUpDate(3, "signupDate", CustomVarScope.visitorLevel),
    userId(4, "userId", CustomVarScope.visitorLevel),
    orderCount(5, "OrderCount", CustomVarScope.visitorLevel),
    ;

    int slot;
    String name;
    CustomVarScope scope;

    CustomVarSlot(int slot, String name, CustomVarScope scope) {
      this.slot = slot;
      this.name = name;
      this.scope = scope;
    }

    public int getSlot() {
      return slot;
    }

    public String getName() {
      return name;
    }

    public CustomVarScope getScope() {
      return scope;
    }
  }

  public static enum CustomVarScope {
    visitorLevel(1),
    sessionLevel(2),
    pageLevel(3);

    int level;

    CustomVarScope(int level) {
      this.level = level;
    }

    public int getLevel() {
      return level;
    }

    @Override
    public String toString() {
      return ""+level;
    }
  }


}