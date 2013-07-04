package com.hk.web.action.admin.accounts;


import com.hk.admin.util.finance.busy.BusyPopulateItemData;
import com.hk.admin.util.finance.busy.BusyPopulateSupplierData;
import com.hk.admin.util.finance.busy.CodPopulateItemData;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.stripesstuff.plugin.security.Secure;

public class PopulateCodCallDataAction {
    private static Logger logger = LoggerFactory.getLogger(PopulateCodCallDataAction.class);
    @Value("#{hkEnvProps['" + Keys.Env.dbHostName + "']}")
    	private String dbHostName;

    	@Value("#{hkEnvProps['" + Keys.Env.dbName + "']}")
    	private String dbName;

    	@Value("#{hkEnvProps['" + Keys.Env.dbUser + "']}")
    	private String dbUser;

    	@Value("#{hkEnvProps['" + Keys.Env.dbPassword + "']}")
    	private String dbPassword;

    @DefaultHandler

    	public Resolution pre() {
    		try {
    			CodPopulateItemData codPopulateItemData = new CodPopulateItemData(dbHostName, dbName, dbUser, dbPassword);

    			logger.info("Populating Cod Call ");
    			codPopulateItemData.populateCodCalldata();


    		} catch (Exception e) {
    			logger.error("Unable to insert: ", e);
    		}
    		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    	}
}
