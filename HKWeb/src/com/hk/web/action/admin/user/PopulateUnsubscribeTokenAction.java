package com.hk.web.action.admin.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.user.PopulateUnsubscribeToken;
import com.hk.constants.core.Keys;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/01/13
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class PopulateUnsubscribeTokenAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(PopulateUserDetailAction.class);

    @Value("#{hkEnvProps['" + Keys.Env.dbHostName + "']}")
    private String dbHostName;

    @Value("#{hkEnvProps['" + Keys.Env.dbName + "']}")
    private String dbName;

    @Value("#{hkEnvProps['" + Keys.Env.dbUser + "']}")
    private String dbUser;

    @Value("#{hkEnvProps['" + Keys.Env.dbPassword + "']}")
    private String dbPassword;

    public Resolution pre() {
        PopulateUnsubscribeToken populateUserDetail = new PopulateUnsubscribeToken(dbHostName, dbName, dbUser, dbPassword);
        populateUserDetail.populateItemData();
        addRedirectAlertMessage(new SimpleMessage("User unsubscribe_tokens upadted!"));
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }
}
