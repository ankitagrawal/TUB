package com.hk.constants.marketing;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

@Component
public class AnalyticsConstants {

    public static String  gaCode;

    public static boolean analytics = true;

    @Value("#{hkEnvProps['" + Keys.Env.analytics + "']}")
    private String        analyticsString;

    @Value("#{hkEnvProps['" + Keys.Env.gaCode + "']}")
    private String        analyticsCode;

    @PostConstruct
    public void postConstruction() {
        // String anaylticsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.analytics);

//        analytics = StringUtils.isNotBlank(analyticsString) && Boolean.parseBoolean(analyticsString);
        analytics = true;

        gaCode = analyticsCode;

    }
}
