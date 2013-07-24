package com.hk.admin.util.finance.busy

import com.hk.domain.core.Pincode
import groovy.sql.Sql
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class BusyPopulateSalesData {
  private String hostName;
  private String dbName;
  private String serverUser;
  private String serverPassword;
  Sql sql;
  Sql busySql;
	public static final String  BUSY_DB_NAME = "healthkart_busy";

  BusyPopulateSalesData(String hostName, String dbName, String serverUser, String serverPassword){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_busy", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");
  }
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateSalesData.class);

  public void transactionHeaderForSalesGenerator() {
    String lastUpdateDate;
    busySql.eachRow("""
                    select max(create_date) as max_date
                    from transaction_header
                    where vch_type = 9
                    and vch_no like '%R%';
                      """){
          lastDate ->
          lastUpdateDate = lastDate.max_date;
      }
    if(lastUpdateDate == null){
      lastUpdateDate = "2009-01-01";
    }

//	  lastUpdateDate = "2013-04-01";

    sql.eachRow("""

							select so.id as shipping_order_id,
							ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) as order_date,
							so.accounting_invoice_number as vch_no,
							u.name as account_name, pm.name as debtors, pm.id as payment_mode_id,
							pay_gate.name as payment_gateway_name,
							a.line1 as address_1, a.line2 as address_2, a.city, a.state,
							w.name as warehouse, w.id as warehouse_id, sum(li.hk_price*li.qty-li.order_level_discount-li.discount_on_hk_price+li.shipping_charge+li.cod_charge) AS net_amount,
							c.name as courier_name,if(so.drop_shipping =1,'DropShip',if(so.is_service_order =1,'Services',if(bo.is_b2b_order=1,'B2B','B2C'))) Order_type,
							so.shipping_order_status_id , ship.return_date as return_date, bo.gateway_order_id, aw.awb_number
							from line_item li
							inner join shipping_order so on li.shipping_order_id=so.id
							inner join base_order bo on so.base_order_id = bo.id
							left join payment p ON bo.payment_id = p.id
							left join payment_mode pm ON pm.id = p.payment_mode_id
							inner join user u on bo.user_id = u.id
							inner join address a ON bo.address_id = a.id
							left join shipment ship on ship.id = so.shipment_id
							left join awb aw on ship.awb_id=aw.id
							left join courier c on aw.courier_id = c.id
							left join gateway pay_gate on p.gateway_id = pay_gate.id
							inner join warehouse w on w.id = so.warehouse_id
							where (((so.shipping_order_status_id in (180, 190, 200,210, 220, 230, 250, 260) OR bo.order_status_id in (30,40,45,50,60,70)) and so.shipping_order_status_id <> 999))
							and ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) >= ${lastUpdateDate}
							and ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) > '2011-11-08 19:59:36'
							and (so.is_service_order <> 1 or so.is_service_order is null)
							and (bo.is_b2b_order <> 1 or bo.is_b2b_order is null)
							GROUP BY so.id
							ORDER BY ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) ASC
                 """) {
      accountingInvoice ->

      Long shippingOrderId;
      String series;
      Date date;
      String vch_no;
  //    String vch_prefix;
      int vch_type;
      String sale_type;
      String account_name;
      String debtors;
      String address_1;
      String address_2;
      String city;
      String state;
      String tin_number;
      String material_centre;
      String narration;
      byte out_of_state;
      String against_form;
      Double net_amount;
      byte imported_flag;

	  String gateway_order_id;
	  String awb_number;

      shippingOrderId = accountingInvoice.shipping_order_id
	     Long warehouseId =  accountingInvoice.warehouse_id;

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
        else if(warehouseId == 1000 ){
            series = "CHD";
        }
        else if(warehouseId == 1001){
            series = "GK";
        }


      date = accountingInvoice.order_date;
/*
      if(accountingInvoice.Order_type.equals("B2B")){
        vch_prefix = "T";
      }
      else if(accountingInvoice.Order_type.equals("Services")){
        vch_prefix = "S";
      }
      else{
        vch_prefix = "R";
      }

      vch_no = vch_prefix+accountingInvoice.vch_no;
 */
	   vch_no = accountingInvoice.vch_no;
      vch_type = 9;

      //Following is for RTO. to be used when busy's RTO model goes live
      /*if(accountingInvoice.shipping_order_status_id == 200){
        vch_type = 3;
        date =   accountingInvoice.return_date;
      }
      else{
        vch_type = 9;
      }*/

      if(accountingInvoice.Order_type.equals("Services")){
		    sale_type = "SERVICE TAX";
	    }
	    else{
        sale_type = "VAT TAX INC";
	    }
      account_name = accountingInvoice.account_name;

	  if(account_name.length() >=40){
		  account_name = account_name.substring(0,39);
	  }

		if(accountingInvoice.payment_mode_id == 40){
			debtors  = "COD_"+accountingInvoice.courier_name;
		}
		else if (accountingInvoice.payment_mode_id == 1000){
			debtors = accountingInvoice.payment_gateway_name;
		}
		else {
			debtors = accountingInvoice.debtors;
		}

      address_1 = accountingInvoice.address_1;
      address_2 = accountingInvoice.address_2;
      city = accountingInvoice.city;
      state = accountingInvoice.state;

      if (address_1 == null) {
        address_1 = "";
      } else if (address_1.length() > 40) {
        address_1 = address_1.substring(0, 39);
      }

      if (address_2 == null) {
        address_2 = "";
      } else if (address_2.length() > 40) {
        address_2 = address_2.substring(0, 39);
      }
      if (city == null) {
        city = "";
      } else if (city.length() > 40) {
        city = city.substring(0, 39);
      }
      if (state == null) {
        state = "";
      } else if (state.length() > 40) {
        state = state.substring(0, 39);
      }

      if ("haryana".equalsIgnoreCase(state)) {
        out_of_state = 0;
      } else {
        out_of_state = 1;
      }

   //  material_centre = accountingInvoice.warehouse;

	    if(warehouseId == 1 || warehouseId == 10 || warehouseId == 101){
          material_centre = "Gurgaon Warehouse";
	      }
	      else if(warehouseId == 2 || warehouseId == 20){
		      material_centre = "Mumbai Warehouse";
	      }
	      else if(warehouseId == 301){
			      material_centre = "Punjabi Bagh Store";
		    }
	      else if(warehouseId == 999){
			      material_centre = "Corporate Office";
		    }
	      else if(warehouseId == 401){
			      material_centre = "Kapashera Warehouse";
		    }
        else if(warehouseId == 1000 ){
            material_centre = "Chandigarh Aqua Store";
        }
        else if(warehouseId == 1001){
            material_centre = "Greater Kailash Aqua Store";
        }
      net_amount = accountingInvoice.net_amount;
      imported_flag = 0;
      tin_number = " ";
      against_form  = " "
      narration = " ";

	  gateway_order_id = accountingInvoice.gateway_order_id;
	  awb_number = accountingInvoice.awb_number;

     try{
      def keys= busySql.executeInsert("""
    INSERT INTO transaction_header 
      (
        series, date, vch_no, vch_type, sale_type, account_name, debtors, address_1, address_2, address_3, address_4, tin_number, material_centre,
        narration, out_of_state, against_form, net_amount, imported, create_date, hk_ref_no, gateway_order_id, awb_number)

        VALUES (${series}, ${date}, ${vch_no}, ${vch_type}, ${sale_type}, ${account_name}, ${debtors}, ${address_1}, ${address_2}, ${city}, ${state}, ${tin_number}, ${material_centre},
        ${narration}, ${out_of_state}, ${against_form}, ${net_amount}, ${imported_flag}, NOW(), ${shippingOrderId}, ${gateway_order_id}, ${awb_number}
      )
      ON DUPLICATE KEY UPDATE
      series = ${series},
      date = ${date},
      vch_no = ${vch_no},
      vch_type = ${vch_type},
      sale_type = ${sale_type},
      account_name = ${account_name},
      debtors = ${debtors},
      address_1 =  ${address_1},
      address_2 = ${address_2},
      address_3 = ${city},
      address_4 = ${state},
      tin_number = ${tin_number},
      material_centre = ${material_centre},
      narration = ${narration},
      out_of_state = ${out_of_state},
      against_form = ${against_form},
      net_amount = ${net_amount},
      imported = ${imported_flag},
      create_date = NOW(),
      hk_ref_no = ${shippingOrderId},
      gateway_order_id = ${gateway_order_id},
      awb_number = ${awb_number}
     """)
       Long vch_code=keys[0][0];
       transactionBodyForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
       transactionFooterForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
    }
      catch (Exception e) {
            logger.info("Unable to insert in  transaction header for SO ID : "+ accountingInvoice.shipping_order_id);
          }
    }
  }

	public void transactionHeaderForServiceSalesGenerator() {
    String lastUpdateDate;

	  lastUpdateDate = "2013-04-01";

    sql.eachRow("""

							select so.id as shipping_order_id,
							ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) as order_date,
							so.accounting_invoice_number as vch_no,
							u.name as account_name, pm.name as debtors, pm.id as payment_mode_id,
							pay_gate.name as payment_gateway_name,
							a.line1 as address_1, a.line2 as address_2, a.city, a.state,
							w.name as warehouse, w.id as warehouse_id, sum(li.hk_price*li.qty-li.order_level_discount-li.discount_on_hk_price+li.shipping_charge+li.cod_charge) AS net_amount,
							c.name as courier_name,if(so.drop_shipping =1,'DropShip',if(so.is_service_order =1,'Services',if(bo.is_b2b_order=1,'B2B','B2C'))) Order_type,
							so.shipping_order_status_id , ship.return_date as return_date, th.hk_ref_no, bo.gateway_order_id, aw.awb_number
							from line_item li
							inner join shipping_order so on li.shipping_order_id=so.id
							inner join base_order bo on so.base_order_id = bo.id
							left join payment p ON bo.payment_id = p.id
							left join payment_mode pm ON pm.id = p.payment_mode_id
							inner join user u on bo.user_id = u.id
							inner join address a ON bo.address_id = a.id
							left join shipment ship on ship.id = so.shipment_id
							left join awb aw on ship.awb_id=aw.id
							left join courier c on aw.courier_id = c.id
							left join gateway pay_gate on p.gateway_id = pay_gate.id
							inner join warehouse w on w.id = so.warehouse_id
							left join healthkart_busy.`transaction_header` th on so.id=th.hk_ref_no
							where (((so.shipping_order_status_id in (180, 190, 200,210, 220, 230, 250, 260) OR bo.order_status_id in (30,40,45,50,60,70)) and so.shipping_order_status_id <> 999))
							and ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) >= ${lastUpdateDate}
							and so.is_service_order = 1
							and th.hk_ref_no is null
							GROUP BY so.id
							ORDER BY ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) ASC
                 """) {
      accountingInvoice ->

      Long shippingOrderId;
      String series;
      Date date;
      String vch_no;
  //    String vch_prefix;
      int vch_type;
      String sale_type;
      String account_name;
      String debtors;
      String address_1;
      String address_2;
      String city;
      String state;
      String tin_number;
      String material_centre;
      String narration;
      byte out_of_state;
      String against_form;
      Double net_amount;
      byte imported_flag;

	  String gateway_order_id;
	  String awb_number;

      shippingOrderId = accountingInvoice.shipping_order_id

      Long warehouseId =  accountingInvoice.warehouse_id;

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
        else if(warehouseId == 1000 ){
            series = "CHD";
        }
        else if(warehouseId == 1001){
            series = "GK";
        }

      date = accountingInvoice.order_date;
/*
      if(accountingInvoice.Order_type.equals("B2B")){
        vch_prefix = "T";
      }
      else if(accountingInvoice.Order_type.equals("Services")){
        vch_prefix = "S";
      }
      else{
        vch_prefix = "R";
      }

      vch_no = vch_prefix+accountingInvoice.vch_no;
 */
	   vch_no = accountingInvoice.vch_no;
      vch_type = 9;

      //Following is for RTO. to be used when busy's RTO model goes live
      /*if(accountingInvoice.shipping_order_status_id == 200){
        vch_type = 3;
        date =   accountingInvoice.return_date;
      }
      else{
        vch_type = 9;
      }*/

      if(accountingInvoice.Order_type.equals("Services")){
		    sale_type = "SERVICE TAX";
	    }
	    else{
        sale_type = "VAT TAX INC";
	    }
      account_name = accountingInvoice.account_name;

	  if(account_name.length() >=40){
		  account_name = account_name.substring(0,39);
	  }

		if(accountingInvoice.payment_mode_id == 40){
			debtors  = "COD_"+accountingInvoice.courier_name;
		}
		else if (accountingInvoice.payment_mode_id == 1000){
			debtors = accountingInvoice.payment_gateway_name;
		}
		else {
			debtors = accountingInvoice.debtors;
		}

      address_1 = accountingInvoice.address_1;
      address_2 = accountingInvoice.address_2;
      city = accountingInvoice.city;
      state = accountingInvoice.state;

      if (address_1 == null) {
        address_1 = "";
      } else if (address_1.length() > 40) {
        address_1 = address_1.substring(0, 39);
      }

      if (address_2 == null) {
        address_2 = "";
      } else if (address_2.length() > 40) {
        address_2 = address_2.substring(0, 39);
      }
      if (city == null) {
        city = "";
      } else if (city.length() > 40) {
        city = city.substring(0, 39);
      }
      if (state == null) {
        state = "";
      } else if (state.length() > 40) {
        state = state.substring(0, 39);
      }

      if ("haryana".equalsIgnoreCase(state)) {
        out_of_state = 0;
      } else {
        out_of_state = 1;
      }

//      material_centre = accountingInvoice.warehouse;
	    if(warehouseId == 1 || warehouseId == 10 || warehouseId == 101){
          material_centre = "Gurgaon Warehouse";
	      }
	      else if(warehouseId == 2 || warehouseId == 20){
		      material_centre = "Mumbai Warehouse";
	      }
	      else if(warehouseId == 301){
			      material_centre = "Punjabi Bagh Store";
		    }
	      else if(warehouseId == 999){
			      material_centre = "Corporate Office";
		    }
	      else if(warehouseId == 401){
			      material_centre = "Kapashera Warehouse";
		    }
        else if(warehouseId == 1000 ){
            material_centre = "Chandigarh Aqua Store";
        }
        else if(warehouseId == 1001){
            material_centre = "Greater Kailash Aqua Store";
        }
      net_amount = accountingInvoice.net_amount;
      imported_flag = 0;
      tin_number = " ";
      against_form  = " "
      narration = " ";

	  gateway_order_id = accountingInvoice.gateway_order_id;
	  awb_number = accountingInvoice.awb_number;  

     try{
      def keys= busySql.executeInsert("""
    INSERT INTO transaction_header
      (
        series, date, vch_no, vch_type, sale_type, account_name, debtors, address_1, address_2, address_3, address_4, tin_number, material_centre,
        narration, out_of_state, against_form, net_amount, imported, create_date, hk_ref_no, gateway_order_id, awb_number)

        VALUES (${series}, ${date}, ${vch_no}, ${vch_type}, ${sale_type}, ${account_name}, ${debtors}, ${address_1}, ${address_2}, ${city}, ${state}, ${tin_number}, ${material_centre},
        ${narration}, ${out_of_state}, ${against_form}, ${net_amount}, ${imported_flag}, NOW(), ${shippingOrderId}, ${gateway_order_id}, ${awb_number}
      )
      ON DUPLICATE KEY UPDATE
      series = ${series},
      date = ${date},
      vch_no = ${vch_no},
      vch_type = ${vch_type},
      sale_type = ${sale_type},
      account_name = ${account_name},
      debtors = ${debtors},
      address_1 =  ${address_1},
      address_2 = ${address_2},
      address_3 = ${city},
      address_4 = ${state},
      tin_number = ${tin_number},
      material_centre = ${material_centre},
      narration = ${narration},
      out_of_state = ${out_of_state},
      against_form = ${against_form},
      net_amount = ${net_amount},
      imported = ${imported_flag},
      create_date = NOW(),
      hk_ref_no = ${shippingOrderId},
      gateway_order_id = ${gateway_order_id},
      awb_number = ${awb_number}
     """)
       Long vch_code=keys[0][0];
       transactionBodyForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
       transactionFooterForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
    }
      catch (Exception e) {
            logger.info("Unable to insert in  transaction header: ",e);
          }
    }
  }

	public void transactionHeaderForB2BSalesGenerator() {
    String lastUpdateDate;

	  lastUpdateDate = "2013-04-01";

    sql.eachRow("""
							select so.id as shipping_order_id,
							ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) as order_date,
							so.accounting_invoice_number as vch_no,
							u.name as account_name, pm.name as debtors, pm.id as payment_mode_id,
							pay_gate.name as payment_gateway_name,
							a.line1 as address_1, a.line2 as address_2, a.city, a.state,
							w.name as warehouse, w.id as warehouse_id, sum(li.hk_price*li.qty-li.order_level_discount-li.discount_on_hk_price+li.shipping_charge+li.cod_charge) AS net_amount,
							c.name as courier_name,if(so.drop_shipping =1,'DropShip',if(so.is_service_order =1,'Services',if(bo.is_b2b_order=1,'B2B','B2C'))) Order_type, th.hk_ref_no,
							so.shipping_order_status_id , ship.return_date as return_date, bo.gateway_order_id, aw.awb_number
							from line_item li
							inner join shipping_order so on li.shipping_order_id=so.id
							inner join base_order bo on so.base_order_id = bo.id
							left join payment p ON bo.payment_id = p.id
							left join payment_mode pm ON pm.id = p.payment_mode_id
							inner join user u on bo.user_id = u.id
							inner join address a ON bo.address_id = a.id
							left join shipment ship on ship.id = so.shipment_id
							left join awb aw on ship.awb_id=aw.id
							left join courier c on aw.courier_id = c.id
							left join gateway pay_gate on p.gateway_id = pay_gate.id
							inner join warehouse w on w.id = so.warehouse_id
							left join healthkart_busy.transaction_header th on so.id=th.hk_ref_no
							where (((so.shipping_order_status_id in (180, 190, 200,210, 220, 230, 250, 260) OR bo.order_status_id in (30,40,45,50,60,70)) and so.shipping_order_status_id <> 999))
							and ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) >= ${lastUpdateDate}
							and bo.is_b2b_order = 1
							and th.hk_ref_no is null
							GROUP BY so.id
							ORDER BY ifnull(ship.ship_date,ifnull(p.payment_date, bo.create_dt)) ASC
                 """) {
      accountingInvoice ->

      Long shippingOrderId;
      String series;
      Date date;
      String vch_no;
  //    String vch_prefix;
      int vch_type;
      String sale_type;
      String account_name;
      String debtors;
      String address_1;
      String address_2;
      String city;
      String state;
      String tin_number;
      String material_centre;
      String narration;
      byte out_of_state;
      String against_form;
      Double net_amount;
      byte imported_flag;

	  String gateway_order_id;
	  String awb_number;  

      shippingOrderId = accountingInvoice.shipping_order_id

      Long warehouseId =  accountingInvoice.warehouse_id;

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
        else if(warehouseId == 1000 ){
            series = "CHD";
        }
        else if(warehouseId == 1001){
            series = "GK";
        }


          date = accountingInvoice.order_date;
/*
      if(accountingInvoice.Order_type.equals("B2B")){
        vch_prefix = "T";
      }
      else if(accountingInvoice.Order_type.equals("Services")){
        vch_prefix = "S";
      }
      else{
        vch_prefix = "R";
      }

      vch_no = vch_prefix+accountingInvoice.vch_no;
 */
	   vch_no = accountingInvoice.vch_no;
      vch_type = 9;

      //Following is for RTO. to be used when busy's RTO model goes live
      /*if(accountingInvoice.shipping_order_status_id == 200){
        vch_type = 3;
        date =   accountingInvoice.return_date;
      }
      else{
        vch_type = 9;
      }*/

      if(accountingInvoice.Order_type.equals("Services")){
		    sale_type = "SERVICE TAX";
	    }
	    else{
        sale_type = "VAT TAX INC";
	    }
      account_name = accountingInvoice.account_name;

	  if(account_name.length() >=40){
		  account_name = account_name.substring(0,39);
	  }

		if(accountingInvoice.payment_mode_id == 40){
			debtors  = "COD_"+accountingInvoice.courier_name;
		}
		else if (accountingInvoice.payment_mode_id == 1000){
			debtors = accountingInvoice.payment_gateway_name;
		}
		else {
			debtors = accountingInvoice.debtors;
		}

      address_1 = accountingInvoice.address_1;
      address_2 = accountingInvoice.address_2;
      city = accountingInvoice.city;
      state = accountingInvoice.state;

      if (address_1 == null) {
        address_1 = "";
      } else if (address_1.length() > 40) {
        address_1 = address_1.substring(0, 39);
      }

      if (address_2 == null) {
        address_2 = "";
      } else if (address_2.length() > 40) {
        address_2 = address_2.substring(0, 39);
      }
      if (city == null) {
        city = "";
      } else if (city.length() > 40) {
        city = city.substring(0, 39);
      }
      if (state == null) {
        state = "";
      } else if (state.length() > 40) {
        state = state.substring(0, 39);
      }

      if ("haryana".equalsIgnoreCase(state)) {
        out_of_state = 0;
      } else {
        out_of_state = 1;
      }

 //     material_centre = accountingInvoice.warehouse;
	    if(warehouseId == 1 || warehouseId == 10 || warehouseId == 101){
          material_centre = "Gurgaon Warehouse";
	      }
	      else if(warehouseId == 2 || warehouseId == 20){
		      material_centre = "Mumbai Warehouse";
	      }
	      else if(warehouseId == 301){
			      material_centre = "Punjabi Bagh Store";
		    }
	      else if(warehouseId == 999){
			      material_centre = "Corporate Office";
		    }
	      else if(warehouseId == 401){
			      material_centre = "Kapashera Warehouse";
		    }
        else if(warehouseId == 1000 ){
            material_centre = "Chandigarh Aqua Store";
        }
        else if(warehouseId == 1001){
            material_centre = "Greater Kailash Aqua Store";
        }
      net_amount = accountingInvoice.net_amount;
      imported_flag = 0;
      tin_number = " ";
      against_form  = " "
      narration = " ";

	  gateway_order_id = accountingInvoice.gateway_order_id;
	  awb_number = accountingInvoice.awb_number;
	    
     try{
      def keys= busySql.executeInsert("""
    INSERT INTO transaction_header
      (
        series, date, vch_no, vch_type, sale_type, account_name, debtors, address_1, address_2, address_3, address_4, tin_number, material_centre,
        narration, out_of_state, against_form, net_amount, imported, create_date, hk_ref_no, gateway_order_id, awb_number)

        VALUES (${series}, ${date}, ${vch_no}, ${vch_type}, ${sale_type}, ${account_name}, ${debtors}, ${address_1}, ${address_2}, ${city}, ${state}, ${tin_number}, ${material_centre},
        ${narration}, ${out_of_state}, ${against_form}, ${net_amount}, ${imported_flag}, NOW(), ${shippingOrderId}, ${gateway_order_id}, ${awb_number}
      )
      ON DUPLICATE KEY UPDATE
      series = ${series},
      date = ${date},
      vch_no = ${vch_no},
      vch_type = ${vch_type},
      sale_type = ${sale_type},
      account_name = ${account_name},
      debtors = ${debtors},
      address_1 =  ${address_1},
      address_2 = ${address_2},
      address_3 = ${city},
      address_4 = ${state},
      tin_number = ${tin_number},
      material_centre = ${material_centre},
      narration = ${narration},
      out_of_state = ${out_of_state},
      against_form = ${against_form},
      net_amount = ${net_amount},
      imported = ${imported_flag},
      create_date = NOW(),
      hk_ref_no = ${shippingOrderId},
      gateway_order_id = ${gateway_order_id},
      awb_number = ${awb_number}
     """)
       Long vch_code=keys[0][0];
       transactionBodyForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
       transactionFooterForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
    }
      catch (Exception e) {
            logger.info("Unable to insert in  transaction header: ",e);
          }
    }
  }

  public void transactionBodyForSalesGenerator(Long vch_code, Long shipping_order_id) {
      int s_no = 0;
      sql.eachRow("""
                      select li.id, li.sku_id, li.qty, li.marked_price, li.hk_price, li.discount_on_hk_price, li.reward_point_discount,
                      t.value as tax_value,li.order_level_discount, li.cost_price
                      from line_item li
                      inner join tax t on li.tax_id = t.id
                      where li.shipping_order_id = ${shipping_order_id}
                   """) {
        invoiceItems ->

      Long lineItemId = invoiceItems.id;
      Long item_code = invoiceItems.sku_id;
      int qty = invoiceItems.qty;
      if(qty <= 0){
        qty = 1;
      }

      String unit = "pcs";
      Double mrp = invoiceItems.marked_price;
      Double discount = (invoiceItems.discount_on_hk_price/qty + invoiceItems.order_level_discount/qty);
      Double rate = invoiceItems.hk_price - discount;
      Double vat = invoiceItems.tax_value;
      Double amount = rate*qty;
	  Double cost_price = invoiceItems.cost_price;
      s_no++;
      try{
        busySql.executeInsert("""
        INSERT INTO transaction_body
          (
            vch_code, s_no, item_code, qty, unit, mrp, rate, discount, vat, amount, create_date, hk_ref_no, cost_price
          )

          VALUES (${vch_code}, ${s_no}, ${item_code}, ${qty}, ${unit}, ${mrp}, ${rate}, ${discount}, ${vat}, ${amount}, NOW(), ${lineItemId}, ${cost_price}
          )
          ON DUPLICATE KEY UPDATE
          vch_code = ${vch_code}, s_no = ${s_no}, item_code = ${item_code}, qty = ${qty}, unit = ${unit}, mrp = ${mrp}, rate = ${rate}, discount = ${discount},
          vat = ${vat}, amount = ${amount}, create_date = NOW(), hk_ref_no = ${lineItemId}, cost_price = ${cost_price}
         """)
      }
      catch (Exception e) {
            logger.info("Unable to insert in  transaction body: ",e);
          }
    }
  }


  public void transactionFooterForSalesGenerator(Long vch_code, Long shipping_order_id) {
    sql.eachRow("""
                    select sum(shipping_charge) as shipping_charge, sum(cod_charge) as cod_charge, sum(reward_point_discount) as reward_points
                    from line_item li
                    inner join tax t on li.tax_id = t.id
                    where li.shipping_order_id = ${shipping_order_id}
                    group by shipping_order_id
                 """) {
      footerItems ->

    Double shipping_charge = footerItems.shipping_charge;
    Double cod_charge = footerItems.cod_charge;
	  Double reward_points = footerItems.reward_points;
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


      busySql.executeInsert("""
      INSERT INTO transaction_footer
        (
          vch_code, s_no, type, bill_sundry_name, percent, amount, create_date
        )

        VALUES (${vch_code}, 2, 0, 'cod charge',0 , ${cod_charge}, NOW()
        )
        ON DUPLICATE KEY UPDATE
        vch_code = ${vch_code}, s_no = 2, type = 0, bill_sundry_name = 'cod charge',
        percent = 0, amount = ${cod_charge}, create_date = NOW()
       """)

	    busySql.executeInsert("""
      INSERT INTO transaction_footer
        (
          vch_code, s_no, type, bill_sundry_name, percent, amount, create_date
        )

        VALUES (${vch_code}, 3, 1, 'reward_points_discount',0 , ${reward_points}, NOW()
        )
        ON DUPLICATE KEY UPDATE
        vch_code = ${vch_code}, s_no = 3, type = 1, bill_sundry_name = 'reward_points_discount',
        percent = 0, amount = ${reward_points}, create_date = NOW()
       """)
    }
    catch (Exception e) {
            logger.info("Unable to insert in  transaction footer: ", e);
          }
    }
}
}