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
    private Long piPopulated;
    private Long piReturnPopulated;


    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {

//    Logger.info("Starting Busy Scripts at: " + new Date());
        try {
            BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);
            BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            logger.warn("Populating Items ");
            busyPopulateItemData.populateItemData();

            logger.warn("Populating Suppliers ");
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
            logger.warn("Retail Sales count before populating = "+beforePopulateCount);

            logger.warn("Populating Retail Sales ");
            busyPopulateSalesData.transactionHeaderForSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordRetailSalesCount();
            logger.warn("Retail Sales count after populating = "+afterPopulateCount);

            setRetailSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("Retail sales populated = "+getRetailSalesPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy Retail sales invoices [" +getRetailSalesPopulated()+"] updated !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populateServiceSales() {
        try {
            BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordServiceSalesCount();
            logger.warn("Service Sales count before populating = "+beforePopulateCount);

            logger.warn("Populating Service Sales ");
            busyPopulateSalesData.transactionHeaderForServiceSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordServiceSalesCount();
            logger.warn("Service Sales count after populating = "+afterPopulateCount);

            setServiceSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("Service sales populated = "+getServiceSalesPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy Service sales invoices [" +getServiceSalesPopulated()+"] updated !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populateB2BSales() {
        try {
            BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordB2BSalesCount();
            logger.warn("B2B Sales count before populating = "+beforePopulateCount);

            logger.warn("Populating B2B Sales ");
            busyPopulateSalesData.transactionHeaderForB2BSalesGenerator();

            Long afterPopulateCount = busyPopulateReport.recordB2BSalesCount();
            logger.warn("B2B Sales count after populating = "+afterPopulateCount);

            setB2bSalesPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("B2B Sales populated = "+getB2bSalesPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }

        addRedirectAlertMessage(new SimpleMessage("Busy b2b sales invoices [" +getB2bSalesPopulated()+"] updated !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populateRto() {
        try {
            BusyPopulateRtoData busyPopulateRtoData = new BusyPopulateRtoData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordRtoCount();
            logger.warn("RTO count before populating = "+beforePopulateCount);

            logger.warn("Populating RTO data ");
            busyPopulateRtoData.transactionHeaderForRtoGenerator();

            Long afterPopulateCount = busyPopulateReport.recordRtoCount();
            logger.warn("RTO count after populating = "+afterPopulateCount);

            setRtoPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("RTO invoices populated = "+getRtoPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy RTO invoices [" +getRtoPopulated()+"] updated !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populatePurchases() {
        try {
            BusyPopulatePurchaseData busyPopulatePurchaseData = new BusyPopulatePurchaseData(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordPiCount();
            logger.warn("PI count before populating ="+beforePopulateCount);

            logger.warn("Populating Purchases ");
            busyPopulatePurchaseData.populatePurchaseData();

            Long afterPopulateCount = busyPopulateReport.recordPiCount();
            logger.warn("PI count after populating ="+afterPopulateCount);

            setPiPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("PI populated = "+getPiPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy purchases ["+getPiPopulated()+"] updated !!"));
        return new RedirectResolution("/pages/admin/busyUpdates.jsp");
    }


    @Secure(hasAnyPermissions = {PermissionConstants.POPULATE_BUSY_DATA}, authActionBean = AdminPermissionAction.class)
    public Resolution populatePurchaseReturns() {
        try {
            BusyPopulatePurchaseReturn busyPopulatePurchaseReturn = new BusyPopulatePurchaseReturn(dbHostName, dbName, dbUser, dbPassword, dbBusyName);

            BusyPopulateReport busyPopulateReport = new BusyPopulateReport(dbHostName, dbUser, dbPassword, dbBusyName);

            Long beforePopulateCount = busyPopulateReport.recordPiReturnCount();
            logger.warn("PI Return count before populating ="+beforePopulateCount);

            logger.warn("Populating Purchase Returns ");
            busyPopulatePurchaseReturn.populatePurchaseReturnData();

            Long afterPopulateCount = busyPopulateReport.recordPiReturnCount();
            logger.warn("PI Return count after populating ="+afterPopulateCount);

            setPiReturnPopulated(afterPopulateCount - beforePopulateCount);
            logger.warn("PI Returns populated = "+getPiReturnPopulated());

        } catch (Exception e) {
            logger.error("Unable to insert: ", e);
        }
        addRedirectAlertMessage(new SimpleMessage("Busy purchases returns ["+getPiReturnPopulated()+"] updated !!"));
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

    public Long getPiPopulated() {
        return piPopulated;
    }

    public void setPiPopulated(Long piPopulated) {
        this.piPopulated = piPopulated;
    }

    public Long getPiReturnPopulated() {
        return piReturnPopulated;
    }

    public void setPiReturnPopulated(Long piReturnPopulated) {
        this.piReturnPopulated = piReturnPopulated;
    }
}