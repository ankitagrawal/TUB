
package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Apr 13, 2012
 * Time: 1:34:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class BusyTableTransactionGenerator {
  private String dbName;
  private String serverUser;
  private String serverPassword;
  private String hostName;
  Sql sqlProd;
  Sql sqlReport;
   private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyTableTransactionGenerator.class);

  BusyTableTransactionGenerator(String hostName, String dbName, String serverUser, String serverPassword){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;

    sqlProd = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    sqlReport = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_busy", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");
  }

//  private static org.slf4j.Logger Logger = LoggerFactory.getLogger(BusyTableTransactionGenerator.class);


  public void populatePurchaseData() {

    // create_date is date on which record is created in DB.

    String lastUpdateDate;


    sqlReport.eachRow("""  SELECT  max(create_date) as lastDate FROM  transaction_header where vch_type = 2  """)   {

      updateRow ->

      if (updateRow[0] == null) {

        lastUpdateDate = "2009-01-01";
      }


      else {

        lastUpdateDate = updateRow.lastDate;
      }

      // get  all records from healthkart_dev   to healthkart_busy  where  healthkart_dev.create_date > healthkart_busy.create_date.


      sqlProd.eachRow(""" SELECT  pv.create_dt as createDate ,pv.invoice_number as invoiceNumber,  s.name supName,  s.line1 as supAddress1, s.line2 as supAddress2
                       ,s.state as supState, s.pincode as supPincode , s.tin_number as supTin
                        ,w.name  as warehouseName ,w.state as warehouseState  , pv.final_payable_amount as finalPayable , pv.warehouse_id  as warehouseId
                          ,pv.id  as purchaseInvoiceId, pv.freight_forwarding_charges  as freight_forwarding_charges ,s.id as suppId, pv.invoice_date as purInvoiceDate
                          ,pv.discount as purchaseleveldiscount, min(grn.create_dt) as grn_date
                           FROM purchase_invoice pv  INNER JOIN  supplier s ON  pv.supplier_id = s.id
      	                    INNER JOIN purchase_invoice_has_grn pigrn ON pigrn.purchase_invoice_id = pv.id
      	                    INNER JOIN goods_received_note grn ON grn.id = pigrn.goods_received_note_id
                            INNER JOIN  warehouse w ON  w.id=pv.warehouse_id  WHERE
                           pv.reconcilation_date > ${lastUpdateDate}
                           and pv.reconciled = 1
                           and pv.create_dt > '2011-11-08 16:10:50'
                           GROUP BY pv.id  """) {

        purchaseRow ->

        String supplierState = purchaseRow.supState;
        String warehouseState = purchaseRow.warehouseState;
        String supplierName = purchaseRow.supName;

	      Long warehouseId = purchaseRow.warehouseId;
	      String warehouseName;
	      if(warehouseId == 1 || warehouseId == 10 || warehouseId == 101){
          warehouseName = "Gurgaon Warehouse";
	      }
	      else if(warehouseId == 2 || warehouseId == 20){
		      warehouseName = "Mumbai Warehouse";
	      }
	      else if(warehouseId == 301){
			      warehouseName = "Punjabi Bagh Store";
		    }
	      else if(warehouseId == 999){
			      warehouseName = "Corporate Office";
		    }
	      else if(warehouseId == 401){
			      warehouseName = "Kapashera Warehouse";
		    }

        String series = '';
	      String invoiceDate = purchaseRow.purInvoiceDate;
	      String invoiceNumber =  purchaseRow.invoiceNumber;


	    if(warehouseId == 1 || warehouseId == 10 || warehouseId == 101){
          series = "HR";
	      }
	      else if(warehouseId == 2 || warehouseId == 20){
		       series = "MH";
	      }
	      else if(warehouseId == 301){
			       series = "PB";
		    }
	      else if(warehouseId == 999){
			      series = "HR";
		    }
	      else if(warehouseId == 401){
			      series = "DL";
		    }

	      int sameState = 0;
        if (supplierState.equalsIgnoreCase(warehouseState)) {
          sameState = 0;
        }
        else {
          sameState = 1;
        }

        //Generate  Transaction Header


        def key = sqlReport.executeInsert(""" INSERT INTO transaction_header (  series,
      date ,vch_no  ,  vch_type,   sale_type ,   account_name, debtors ,  address_1,   address_2,   address_3 ,   address_4  ,
      tin_number ,  material_centre  ,    out_of_state  ,    net_amount ,  imported ,  create_date , hk_ref_no
    )
        VALUES(   ${series} , ${invoiceDate} , substring(${invoiceNumber} ,1,25) ,  2 , 'VAT TAX INC'  , substring(${supplierName},1,40)
     , substring(${purchaseRow.suppId },1,40)  , substring(${purchaseRow.supAddress1 },1,40) ,  substring(${purchaseRow.supAddress2 },1,40) , ${supplierState}
          , substring(${purchaseRow.supPincode },1,40),${purchaseRow.supTin} ,substring(${warehouseName},1,40)  , ${sameState} , ${purchaseRow.finalPayable}
         ,0 , NOW() , ${purchaseRow.purchaseInvoiceId} )


 """);

        //RECURSE EVERY ROW
        def arr = (Integer[][]) key;
        int auto_id = arr[0][0];

        // Generate Transaction Body
        //For each purchase id  get all the items corresponds to it
        int count = 0;

        sqlProd.eachRow(""" SELECT t.id , t.value as taxValue, pil. purchase_invoice_id, pil.cost_price as costPrice, pil.sku_id ,pil.qty ,pil.mrp ,pil.discount_percent
                            ,pil.id as purchaseLineItemId, sur.value as surchargeValue, pil.tax_id  as taxId, pil.payable_amount as payableAmount

                    FROM  purchase_invoice_line_item  pil LEFT OUTER JOIN tax t ON t.id = pil.tax_id
                    LEFT OUTER JOIN surcharge sur  ON pil.surcharge_id = sur.id  WHERE  pil.purchase_invoice_id =${purchaseRow.purchaseInvoiceId}
                    """) {

          pilRow ->
          count = count + 1;



          try {

            Float finalVat = 0.0;
            Float rate = pilRow.costPrice;
            Float costPriceFinal = pilRow.payableAmount;
            Float discount = pilRow.discount_percent;
            int quantity = pilRow.qty;
            if (quantity == 0) {
              quantity = 1;
            }
            if (rate == null) {
              rate = 0.0;
            }

            if (costPriceFinal == null) {
              costPriceFinal = 0.0;
            }

            if (sameState == 1) {
              if (pilRow.surchargeValue != null) {
                finalVat = pilRow.surchargeValue;
              }
              else {
                finalVat = 0.0;
              }
            }
            else if (sameState == 0) {

              if (pilRow.taxId != null) {
                finalVat = pilRow.taxValue;
              }
              else {
                finalVat = pilRow.surchargeValue;
              }


            }

            sqlReport.execute("""

      INSERT INTO transaction_body ( vch_code , s_no, item_code , qty , unit , mrp , rate , discount , vat  , amount , create_date , hk_ref_no)

      VALUES( ${auto_id} , ${count}  , ${ pilRow.sku_id} , ${quantity} ,  'Pcs' ,${ pilRow.mrp} ,${rate}, ${discount} , ${finalVat}
          , ${costPriceFinal} ,NOW() , ${ pilRow.purchaseLineItemId}  )  """);


          } catch (Exception e1) {
            Logger.info("insertion into transaction_body is failed",e1);
          }


        }

        // Generate Transaction  Footer
        try {

          sqlReport.execute("""

            INSERT INTO transaction_footer ( vch_code ,s_no, type , bill_sundry_name , percent ,amount ,create_date )

       VALUES( ${auto_id} ,1, 0,  'freight_forwarding_charges' ,null ,  ifnull( ${purchaseRow.freight_forwarding_charges},0)
       , NOW() )

     """);

          sqlReport.execute("""

            INSERT INTO transaction_footer ( vch_code ,s_no, type , bill_sundry_name , percent ,amount ,create_date )

       VALUES( ${auto_id} ,2, 1,  'discount' ,null ,  ifnull( ${purchaseRow.purchaseleveldiscount},0)
       , NOW() )

     """);


        } catch (Exception e2) {
          logger.info("Unable to insert in  supplier: ", e);
        }
      }
    }


  }
}









