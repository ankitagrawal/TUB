package com.hk.web.action.admin.accounts;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.finance.busy.CodPopulateItemData;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

public class PopulateCodCallDataAction extends BaseAction {
    private static Logger logger = LoggerFactory.getLogger(PopulateCodCallDataAction.class);
    @Value("#{hkEnvProps['" + Keys.Env.dbHostName + "']}")
    private String dbHostName;

    @Value("#{hkEnvProps['" + Keys.Env.dbName + "']}")
    private String dbName;

    @Value("#{hkEnvProps['" + Keys.Env.dbUser + "']}")
    private String dbUser;

    @Value("#{hkEnvProps['" + Keys.Env.dbPassword + "']}")
    private String dbPassword;

    private Integer totalRowsUpdateCodCall;

    @DefaultHandler
    public Resolution pre() {
        try {
            CodPopulateItemData codPopulateItemData = new CodPopulateItemData(dbHostName, dbName, dbUser, dbPassword);

            logger.info("Populating Cod Call ");
            totalRowsUpdateCodCall = codPopulateItemData.populateCodCalldata();
            addRedirectAlertMessage(new SimpleMessage("total rows updated", totalRowsUpdateCodCall.toString()));
        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        return new RedirectResolution("/pages/admin/adminHome.jsp");
    }

}
