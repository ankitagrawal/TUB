package com.hk.web.action.admin.accounts;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.finance.busy.BusyPopulateItemData;
import com.hk.admin.util.finance.busy.BusyPopulateSalesData;
import com.hk.admin.util.finance.busy.BusyPopulateSupplierData;
import com.hk.admin.util.finance.busy.BusyTableTransactionGenerator;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class PopulateBusyDataAction extends BaseAction {

  private static Logger logger                 = LoggerFactory.getLogger(PopulateBusyDataAction.class);



  public Resolution pre(){
    String hostName = "localhost";
    String dbName = "healthkart_stag";
    String serverUser = "hkadmin";
    String serverPassword = "admin2K11!";
//    Logger.info("Starting Busy Scripts at: " + new Date());
    try{
      BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(hostName, dbName, serverUser, serverPassword);
      BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(hostName, dbName, serverUser, serverPassword);
      BusyTableTransactionGenerator busyTableTransactionGenerator = new BusyTableTransactionGenerator(hostName, dbName, serverUser, serverPassword);
      BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(hostName, dbName, serverUser, serverPassword);

      logger.debug("Populating Items ");
        busyPopulateItemData.populateItemData();
      logger.debug("Populating Suppliers ");
        busyPopulateSupplierData.busySupplierUpdate();
      logger.debug("Populating Sales ");
        busyPopulateSalesData.transactionHeaderForSalesGenerator();
      logger.debug("Populating Purchases ");
        busyTableTransactionGenerator.populatePurchaseData();
    }catch (Exception e){
      logger.error("Unable to insert: ", e);
    }
      addRedirectAlertMessage(new SimpleMessage("Busy data upadted !!"));
     return new ForwardResolution("/pages/admin/adminHome.jsp");
  }
}