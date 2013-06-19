
package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Apr 13, 2012
 * Time: 1:34:30 PM
 * To change this template use File | Settings | File Templates.
 */

public class BusyPopulatePurchaseReturn {
  private String dbName;
  private String serverUser;
  private String serverPassword;
  private String hostName;
  Sql sqlProd;
  Sql sqlReport;
   private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulatePurchaseReturn.class);

    BusyPopulatePurchaseReturn(String hostName, String dbName, String serverUser, String serverPassword){
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


  public void populatePurchaseReturnData() {

    // create_date is date on which record is created in DB.

    String lastUpdateDate;


    sqlReport.eachRow("""  SELECT  max(create_date) as lastDate FROM  transaction_header where vch_type = 10  """)   {

      updateRow ->

      if (updateRow[0] == null) {

        lastUpdateDate = "2009-01-01";
      }


      else {

        lastUpdateDate = updateRow.lastDate;
      }

      // get  all records from healthkart_dev   to healthkart_busy  where  healthkart_dev.create_date > healthkart_busy.create_date.
    }

      sqlProd.eachRow(""" SELECT dn.debit_note_number as vch_no, dn.create_date as date, s.name as account_name,
                            s.line1 as address_1, s.line2 as address_2, s.state as address_3, s.pincode as address_4,
                            s.tin_numbner as tin_number, dn.id as hk_ref_no, w.id as warehouseId, w.state as warehouseState, s.id as supplierId

                            FROM debit_note dn
                            INNER JOIN supplier s on s.id=dn.supplier_id
                            INNER JOIN warehouse w on w.id=dn.warehouse_id
                            WHERE dn.debit_note_status_id = 100
                            AND dn.closing_date > ${lastUpdateDate}  """) {

        debitNote ->


          String date = debitNote.date;
	      Long warehouseId = debitNote.warehouseId;
          String supplierState = debitNote.address_3;
          String warehouseState = debitNote.warehouseState;
          byte out_of_state;
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

        if (supplierState.equalsIgnoreCase(warehouseState)) {
          out_of_state = 0;
        }
        else {
            out_of_state = 1;
        }
        String material_centre = substring(warehouseName, 1, 40);
        String address_1 = substring(debitNote.address_1, 1, 40);
        String address_2 = substring(debitNote.address_2, 1, 40);
        String address_3 = substring(debitNote.address_3, 1, 40);
        String address_4 = substring(debitNote.address_4, 1, 40);
        String tin_number = substring(debitNote.tin_number, 1, 30);
        String account_name = substring(debitNote.account_name, 1, 40);
        String debtors = debitNote.supplierId;
        int vch_type=10;
        String vch_no = substring(debitNote.vch_no, 1 ,25);
        String sale_type = "VAT TAX INC";
        double net_amount;
        byte imported = 0;
        long hk_ref_no  = debitNote.hk_ref_no;
        //Generate  Transaction Header


            def keys = sqlReport.executeInsert(""" INSERT INTO transaction_header (  series,
      date ,vch_no  ,  vch_type,   sale_type ,   account_name, debtors ,  address_1,   address_2,   address_3 ,   address_4  ,
      tin_number ,  material_centre  ,    out_of_state  ,    net_amount ,  imported ,  create_date , hk_ref_no
    )
        VALUES(   ${series} , ${date} , ${vch_no} ,  ${vch_type} , ${sale_type}  , ${account_name}
     , ${debtors}  , ${address_1} ,  ${address_2} , ${address_3}
          , ${address_4}, ${tin_number} , ${material_centre}  , ${out_of_state} , ${net_amount}
         , ${imported} , NOW() , ${hk_ref_no})


 """);

        //RECURSE EVERY ROW
            Long vch_code=keys[0][0];
  //          transactionBodyForPurchaseReturn(vch_code, hk_ref_no);
    //        transactionFooterForPurchaseReturn(vch_code, hk_ref_no);




      }



  }

    public void transactionBodyForPurchaseReturn(Long vch_code, Long debit_note_id) {
        int s_no = 0;
        sql.eachRow("""
                      select li.id, li.sku_id, li.qty, li.mrp, li.cost_price, li. li.
                      t.value as tax_value,
                      from debit_note_line_item li
                      inner join tax t on li.tax_id = t.id
                      where li.debit_note_id = ${debit_note_id}
                   """) {
            debitNoteItem ->

                Long lineItemId = debitNoteItem.id;
                Long item_code = debitNoteItem.sku_id;
                int qty = debitNoteItem.qty;
                if(qty <= 0){
                    qty = 1;
                }

                String unit = "pcs";
                Double mrp = debitNoteItem.mrp;
                Double rate = debitNoteItem.cost_price;
                Double vat = debitNoteItem.tax_value;
                Double amount = rate*qty;
                s_no++;
                try{
                    busySql.executeInsert("""
        INSERT INTO transaction_body
          (
            vch_code, s_no, item_code, qty, unit, mrp, rate, discount, vat, amount, create_date, hk_ref_no, cost_price
          )

          VALUES (${vch_code}, ${s_no}, ${item_code}, ${qty}, ${unit}, ${mrp}, ${rate}, 0, ${vat}, ${amount}, NOW(), ${lineItemId}, NULL
          )
          ON DUPLICATE KEY UPDATE
          vch_code = ${vch_code}, s_no = ${s_no}, item_code = ${item_code}, qty = ${qty}, unit = ${unit}, mrp = ${mrp}, rate = ${rate}, discount = NULL,
          vat = ${vat}, amount = ${amount}, create_date = NOW(), hk_ref_no = ${lineItemId}, cost_price = NULL
         """)
                }
                catch (Exception e) {
                    logger.info("Unable to insert in  transaction body: ",e);
                }
        }
    }


    public void transactionFooterForPurchaseReturn(Long vch_code, Long debit_note_id) {
        sql.eachRow("""
                    select freight_forwarding_charges as shipping_charge FROM debit_note dn
                    where dn.id = ${debit_note_id}
                 """) {
            footerItems ->

                Double shipping_charge = footerItems.shipping_charge;
                try{
                    busySql.executeInsert("""
      INSERT INTO transaction_footer
        (
          vch_code, s_no, type, bill_sundry_name, percent, amount, create_date
        )

        VALUES (${vch_code}, 1, 0, 'shipping charge',0 , ${shipping_charge}, NOW()
        )
         ON DUPLICATE KEY UPDATE
        vch_code = ${vch_code}, s_no = 1, type = 0, bill_sundry_name = 'shipping charge',
        percent = 0, amount = ${shipping_charge}, create_date = NOW()
       """)



                }
                catch (Exception e) {
                    logger.info("Unable to insert in  transaction footer: ", e);
                }
        }
    }

}









