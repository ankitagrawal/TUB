package com.hk.web.action.admin.accounts;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.finance.busy.*;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


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

	@DefaultHandler
	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution pre() {

//    Logger.info("Starting Busy Scripts at: " + new Date());
		try {
			BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(dbHostName, dbName, dbUser, dbPassword);
			BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating Items ");
			busyPopulateItemData.populateItemData();

			logger.info("Populating Suppliers ");
			busyPopulateSupplierData.busySupplierUpdate();

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateSales() {
		try {
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating Sales ");
			busyPopulateSalesData.transactionHeaderForSalesGenerator();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy Retail sales upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateServiceSales() {
		try {
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating Sales ");
			busyPopulateSalesData.transactionHeaderForServiceSalesGenerator();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy Service sales upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateB2BSales() {
		try {
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating Sales ");
			busyPopulateSalesData.transactionHeaderForB2BSalesGenerator();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}

		addRedirectAlertMessage(new SimpleMessage("Busy b2b sales upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateRto() {
		try {
			BusyPopulateRtoData busyPopulateRtoData = new BusyPopulateRtoData(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating RTO data ");
			busyPopulateRtoData.transactionHeaderForRtoGenerator();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy RTO data upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populatePurchases() {
		try {
			BusyTableTransactionGenerator busyTableTransactionGenerator = new BusyTableTransactionGenerator(dbHostName, dbName, dbUser, dbPassword);

			logger.info("Populating Purchases ");
			busyTableTransactionGenerator.populatePurchaseData();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy purchases upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}

    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populatePurchaseReturns() {
        try {
            BusyPopulatePurchaseReturn busyPopulatePurchaseReturn = new BusyPopulatePurchaseReturn(dbHostName, dbName, dbUser, dbPassword);

            logger.info("Populating Purchases ");
            busyPopulatePurchaseReturn.populatePurchaseReturnData();
        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy purchases returns upadted !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }
}