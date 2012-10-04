package com.hk.constants.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;

/**
 * Created with IntelliJ IDEA. User: Pradeep Date: 9/7/12 Time: 4:52 AM
 */
@Component
public class EmailConstants {

    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyEmail + "']}")
    private static String hkNoReplyEmail;

    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyName + "']}")
    private static String hkNoReplyName;

    @Value("#{hkEnvProps['" + Keys.Env.hkContactEmail + "']}")
    private static String hkContactEmail;

    @Value("#{hkEnvProps['" + Keys.Env.hkContactName + "']}")
    private static String hkContactName;

    public static String getHkNoReplyEmail() {
        return hkNoReplyEmail;
    }

    public static String getHkNoReplyName() {
        return hkNoReplyName;
    }

    public static String getHkContactEmail() {
        return hkContactEmail;
    }

    public static String getHkContactName() {
        return hkContactName;
    }
}
