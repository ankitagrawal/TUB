package com.hk.web.action.admin;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.task.TaskService;
import com.hk.admin.util.finance.busy.BusyPopulateItemData;
import com.hk.admin.util.finance.busy.BusyPopulateSupplierData;
import com.hk.admin.util.finance.busy.BusyTableTransactionGenerator;
import com.hk.admin.util.finance.busy.BusyPopulateSalesData;

import java.util.Date;


/*@Secure(hasAnyPermissions = { PermissionConstants.RUN_ANT_BUILDS })*/
@Component
public class TaskManagerAction extends BaseAction {

  @Autowired
  TaskService             taskService;
/*
  @Autowired
  ProductCatalogServiceImpl productManager;
*/

  private String db_master_service;
  private static Logger logger                 = LoggerFactory.getLogger(TaskManagerAction.class);


  @DefaultHandler
  public Resolution pre(){
     return new ForwardResolution("/pages/admin/taskManager.jsp");
  }
  public Resolution db_master() {
        boolean status = taskService.execute(db_master_service);
        if(status){
          addRedirectAlertMessage(new SimpleMessage("DB Master ran successfully"));
        }
        else{
          addRedirectAlertMessage(new SimpleMessage("DB Master failed"));
        }
        return new ForwardResolution("/pages/admin/taskManager.jsp");
  }

  public Resolution populate_busy(){
    String hostName = "localhost";
    String dbName = "healthkart_stag";
    String serverUser = "root";
    String serverPassword = "admin2K11!";
//    Logger.info("Starting Busy Scripts at: " + new Date());
    try{
      BusyPopulateItemData busyPopulateItemData = new BusyPopulateItemData(hostName, dbName, serverUser, serverPassword);
      BusyPopulateSupplierData busyPopulateSupplierData = new BusyPopulateSupplierData(hostName, dbName, serverUser, serverPassword);
      BusyTableTransactionGenerator busyTableTransactionGenerator = new BusyTableTransactionGenerator(hostName, dbName, serverUser, serverPassword);
      BusyPopulateSalesData busyPopulateSalesData = new BusyPopulateSalesData(hostName, dbName, serverUser, serverPassword);

      System.out.println("Populating Items ");
        busyPopulateItemData.populateItemData();
      System.out.println("Populating Suppliers ");
        busyPopulateSupplierData.busySupplierUpdate();
      System.out.println("Populating Sales ");
        busyPopulateSalesData.transactionHeaderForSalesGenerator();
      System.out.println("Populating Purchases ");
        busyTableTransactionGenerator.populatePurchaseData();
    }catch (Exception e){
      logger.error("Unable to insert: ", e);
    }
     return new ForwardResolution("/pages/admin/taskManager.jsp");
  }

  public String getDb_master_service() {
    return db_master_service;
  }

  public void setDb_master_service(String db_master_service) {
    this.db_master_service = db_master_service;
  }
}