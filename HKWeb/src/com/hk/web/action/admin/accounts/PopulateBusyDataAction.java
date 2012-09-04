package com.hk.web.action.admin.accounts;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.finance.busy.*;
import com.hk.constants.core.Keys;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
public class PopulateBusyDataAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(PopulateBusyDataAction.class);


	@Value("#{hkEnvProps['" + Keys.Env.dbHostName + "']}")
	private String dbHostName;

	@Value("#{hkEnvProps['" + Keys.Env.dbName + "']}")
	private String dbName;

	@Value("#{hkEnvProps['" + Keys.Env.dbUser + "']}")
	private String dbUser;

	@Value("#{hkEnvProps['" + Keys.Env.dbPassword + "']}")
	private String dbPassword;


	public Resolution pre() {

//    Logger.info("Starting Busy Scripts at: " + new Date());
		try {
			BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(dbHostName, dbName, dbUser, dbPassword);
			BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(dbHostName, dbName, dbUser, dbPassword);
			BusyTableTransactionGenerator busyTableTransactionGenerator = new BusyTableTransactionGenerator(dbHostName, dbName, dbUser, dbPassword);
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword);
			BusyPopulateRtoData busyPopulateRtoData = new BusyPopulateRtoData(dbHostName, dbName, dbUser, dbPassword);

			logger.debug("Populating Items ");
			busyPopulateItemData.populateItemData();
			logger.debug("Populating Suppliers ");
			busyPopulateSupplierData.busySupplierUpdate();
			logger.debug("Populating Sales ");
			busyPopulateSalesData.transactionHeaderForSalesGenerator();
			logger.debug("Populating Purchases ");
			busyTableTransactionGenerator.populatePurchaseData();
			logger.debug("Populating RTO data ");
			busyPopulateRtoData.transactionHeaderForRtoGenerator();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy data upadted !!"));
		return new ForwardResolution("/pages/admin/adminHome.jsp");
	}
}