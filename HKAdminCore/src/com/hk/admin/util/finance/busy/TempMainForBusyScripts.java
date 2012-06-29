package com.hk.admin.util.finance.busy;

import java.util.Date;

/**
 * Temporary script to populate busy data on staging
 * User: Tarun
 * Date: Jun 29, 2012
 * Time: 11:43:06 AM
 */
public class TempMainForBusyScripts {
  public static void main(String[] args){
    String hostName = "localhost";
    String dbName = "healthkart_qa";
    String serverUser = "root";
    String serverPassword = "p@55word";
    System.out.println("Starting Busy Scripts at: " + new Date());
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
      System.out.println("Unable to insert: "+e);
    }
    System.out.println("Busy Scripts ran at: " + new Date());
  }
}
