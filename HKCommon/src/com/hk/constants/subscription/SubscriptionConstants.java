package com.hk.constants.subscription;

import com.hk.constants.core.Keys;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/10/12
 * Time: 12:02 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SubscriptionConstants {
    public static int minSubscriptionDays;
    public static int maxSubscriptionDays;
    public static int customerBufferDays;
    public static int inventoryBufferDays;

    @Value("#{hkEnvProps['" + Keys.Env.minSubscriptionDays + "']}")
    private String        minSubDaysString;

    @Value("#{hkEnvProps['" + Keys.Env.maxSubscriptionDays + "']}")
    private String        maxSubDaysString;

    @Value("#{hkEnvProps['"+ Keys.Env.customerBufferDays + "']}")
    private String        customerBufferDaysString;

    @Value("#{hkEnvProps['"+ Keys.Env.inventoryBufferDays + "']}")
    private String        inventoryBufferDaysString;


    @PostConstruct
    public void postConstruction() {
        // String anaylticsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.analytics);

        minSubscriptionDays = Integer.parseInt(minSubDaysString);

        maxSubscriptionDays = Integer.parseInt(maxSubDaysString);

        customerBufferDays = Integer.parseInt(customerBufferDaysString);

        inventoryBufferDays = Integer.parseInt(inventoryBufferDaysString);

    }

}
