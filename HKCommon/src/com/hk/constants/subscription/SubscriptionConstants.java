package com.hk.constants.subscription;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

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
    public static int subscriptionCustomerBufferDays;
    public static int subscriptionInventoryBufferDays;

    @Value("#{hkEnvProps['" + Keys.Env.minSubscriptionDays + "']}")
    private String        minSubDaysString;

    @Value("#{hkEnvProps['" + Keys.Env.maxSubscriptionDays + "']}")
    private String        maxSubDaysString;

    @Value("#{hkEnvProps['"+ Keys.Env.subscriptionCustomerBufferDays + "']}")
    private String        customerBufferDays;

    @Value("#{hkEnvProps['"+ Keys.Env.subscriptionInventoryBufferDays + "']}")
    private String        inventoryBufferDays;


    @PostConstruct
    public void postConstruction() {
        // String anaylticsString = (String) ServiceLocatorFactory.getProperty(Keys.Env.analytics);

        minSubscriptionDays = Integer.parseInt(minSubDaysString);

        maxSubscriptionDays = Integer.parseInt(maxSubDaysString);

        subscriptionCustomerBufferDays = Integer.parseInt(customerBufferDays);

        subscriptionInventoryBufferDays = Integer.parseInt(inventoryBufferDays);

    }

}
