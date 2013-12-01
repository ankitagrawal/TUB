package com.hk.web.action.admin.user;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.drishti.PopulateUserDetail;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/22/12
 * Time: 8:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PopulateUserDetailAction extends BaseAction {

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
        PopulateUserDetail populateUserDetail = new PopulateUserDetail(dbHostName, dbName, dbUser, dbPassword);
        populateUserDetail.populateItemData();
        addRedirectAlertMessage(new SimpleMessage("UserDetail table upadted!"));
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }
}
