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

    @Value("#{hkEnvProps['" + Keys.Env.dbBusyName + "']}")
    private String dbBusyName;

    private Long retailSalesPopulated;
    private Long serviceSalesPopulated;
    private Long b2bSalesPopulated;
    private Long rtoPopulated;


	@DefaultHandler
	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution pre() {

//    Logger.info("Starting Busy Scripts at: " + new Date());
		try {
			BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);
			BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

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
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordRetailSalesCount();
            logger.info("Retail Sales count before populating = "+beforePopulateCount);

            logger.info("Populating Retail Sales ");
			busyPopulateSalesData.transactionHeaderForSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordRetailSalesCount();
            logger.info("Retail Sales count after populating = "+afterPopulateCount);

            setRetailSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.info("Retail sales populated = "+getRetailSalesPopulated());

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy Retail sales updated !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}


	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateServiceSales() {
		try {
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordServiceSalesCount();
            logger.info("Service Sales count before populating = "+beforePopulateCount);

			logger.info("Populating Service Sales ");
			busyPopulateSalesData.transactionHeaderForServiceSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordServiceSalesCount();
            logger.info("Service Sales count after populating = "+afterPopulateCount);

            setServiceSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.info("Service sales populated = "+getServiceSalesPopulated());

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy Service sales upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}


	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateB2BSales() {
		try {
			BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordB2BSalesCount();
            logger.info("B2B Sales count before populating = "+beforePopulateCount);

			logger.info("Populating B2B Sales ");
			busyPopulateSalesData.transactionHeaderForB2BSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordB2BSalesCount();
            logger.info("B2B Sales count after populating = "+afterPopulateCount);

            setB2bSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.info("B2B Sales populated = "+getB2bSalesPopulated());

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}

		addRedirectAlertMessage(new SimpleMessage("Busy b2b sales upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}


	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populateRto() {
		try {
			BusyPopulateRtoData busyPopulateRtoData = new BusyPopulateRtoData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordRtoCount();
            logger.info("RTO count before populating = "+beforePopulateCount);

			logger.info("Populating RTO data ");
			busyPopulateRtoData.transactionHeaderForRtoGenerator();

            Long afterPopulateCount = busyPopulateReport.recordRtoCount();
            logger.info("RTO count after populating = "+afterPopulateCount);

            setRtoPopulated(afterPopulateCount - beforePopulateCount);
            logger.info("RTO invoices populated = "+getRtoPopulated());

		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy RTO data upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}


	@Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
	public Resolution populatePurchases() {
		try {
			BusyPopulatePurchaseData busyPopulatePurchaseData = new BusyPopulatePurchaseData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

			logger.info("Populating Purchases ");
			busyPopulatePurchaseData.populatePurchaseData();
		} catch (Exception e) {
			logger.error("Unable to insert: ", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Busy purchases upadted !!"));
		return new RedirectResolution("/pages/admin/busyUpdates.jsp");
	}


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populatePurchaseReturns() {
        try {
            BusyPopulatePurchaseReturn busyPopulatePurchaseReturn = new BusyPopulatePurchaseReturn(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            logger.info("Populating Purchases ");
            busyPopulatePurchaseReturn.populatePurchaseReturnData();
        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy purchases returns upadted !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }

    public Long getRetailSalesPopulated() {
        return retailSalesPopulated;
    }

    public void setRetailSalesPopulated(Long retailSalesPopulated) {
        this.retailSalesPopulated = retailSalesPopulated;
    }

    public Long getServiceSalesPopulated() {
        return serviceSalesPopulated;
    }

    public void setServiceSalesPopulated(Long serviceSalesPopulated) {
        this.serviceSalesPopulated = serviceSalesPopulated;
    }

    public Long getB2bSalesPopulated() {
        return b2bSalesPopulated;
    }

    public void setB2bSalesPopulated(Long b2bSalesPopulated) {
        this.b2bSalesPopulated = b2bSalesPopulated;
    }

    public Long getRtoPopulated() {
        return rtoPopulated;
    }

    public void setRtoPopulated(Long rtoPopulated) {
        this.rtoPopulated = rtoPopulated;
    }
}