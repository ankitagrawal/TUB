package com.hk.constants.marketing;

import com.hk.constants.core.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AnalyticsConstants {

	@Value("#{hkEnvProps['" + Keys.Env.analytics + "']}")
	private String analytics_string;
  private boolean analytics;

	@Value("#{hkEnvProps['" + Keys.Env.gaCode + "']}")
  private String gaCode ="abc";

	@PostConstruct
	public void postConstruction() {
	    this.analytics = Boolean.parseBoolean(analytics_string);
	}

}
