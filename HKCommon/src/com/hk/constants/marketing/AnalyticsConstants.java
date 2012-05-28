package com.hk.constants.marketing;

import com.hk.constants.core.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AnalyticsConstants {

	@Value("#{hkEnvProps['" + Keys.Env.analytics + "']}")
	public static String analytics_string;

	@Value("#{hkEnvProps['" + Keys.Env.gaCode + "']}")
  public static String gaCode ="abc";

  public static boolean analytics;
  
	@PostConstruct
	public void postConstruction() {
	    this.analytics = Boolean.parseBoolean(analytics_string);
	}

}
