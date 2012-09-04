package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class BusyPopulateRtoData {
  private String hostName;
  private String dbName;
  private String serverUser;
  private String serverPassword;
  Sql sql;
  Sql busySql;

  BusyPopulateRtoData(String hostName, String dbName, String serverUser, String serverPassword){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_busy", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");
  }
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateRtoData.class);

  public void transactionHeaderForRtoGenerator() {
    String lastUpdateDate;
    busySql.eachRow("""
                    select max(create_date) as max_date
                    from transaction_header
                    where vch_type = 3;
                      """){
          lastDate ->
          lastUpdateDate = lastDate.max_date;
      }
    if(lastUpdateDate == null){
      lastUpdateDate = "2009-01-01";
    }

    sql.eachRow("""
                    select so.id as shipping_order_id, ship.ship_date as order_date, so.accounting_invoice_number_id as vch_no,
                           u.name as account_name, pm.name as debtors, pm.id as payment_mode_id,
                           a.line1 as address_1, a.line2 as address_2, a.city, a.state,
                           w.name as warehouse, w.id as warehouse_id, so.amount as net_amount, c.name as courier_name,
                           so.is_service_order, bo.is_b2b_order, so.shipping_order_status_id  , ship.return_date as return_date
                    from 	  shipping_order so

                    inner join base_order bo on so.base_order_id = bo.id
                    inner join payment  p ON bo.payment_id = p.id
                    inner join payment_mode pm ON pm.id = p.payment_mode_id
                    inner join user u on bo.user_id = u.id
                    inner join address a ON bo.address_id = a.id
                    inner join shipment ship on ship.id = so.shipment_id
                    inner join courier c on ship.courier_id = c.id
                    inner join warehouse w on w.id = so.warehouse_id

                    where so.shipping_order_status_id = 200
                      and (ship.ship_date > ${lastUpdateDate} and ship.ship_date > '2011-11-08 19:59:36')
                      ORDER BY ship.ship_date ASC
                 """) {
      accountingInvoice ->

      Long shippingOrderId;
      String series;
      Date date;
      String vch_no;
      String vch_prefix;
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
      Long net_amount;
      byte imported_flag;


      shippingOrderId = accountingInvoice.shipping_order_id

      if(accountingInvoice.warehouse_id == 1){
        series = "HR";
      }
      else{
        series = "MH";
      }

      date = accountingInvoice.return_date;

      if(accountingInvoice.is_b2b_order != 0 && accountingInvoice.is_b2b_order == 1){
        vch_prefix = "B";
      }
      else if(accountingInvoice.is_service_order != null && accountingInvoice.is_service_order == 1){
        vch_prefix = "S";
      }
      else{
        vch_prefix = "R";
      }
      vch_no = vch_prefix+accountingInvoice.vch_no;

      vch_type = 3;

      //Following is for RTO. to be used when busy's RTO model goes live
      /*if(accountingInvoice.shipping_order_status_id == 200){
        vch_type = 3;
        date =   accountingInvoice.return_date;
      }
      else{
        vch_type = 9;
      }*/

      sale_type = "VAT TAX INC";
      account_name = accountingInvoice.account_name;

      if(accountingInvoice.payment_mode_id == 40){
        debtors  = "COD_"+accountingInvoice.courier_name;
      }
      else{
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

      material_centre = accountingInvoice.warehouse;
      net_amount = accountingInvoice.net_amount;
      imported_flag = 0;
      tin_number = " ";
      against_form  = " "
      narration = " ";

     try{
      def keys= busySql.executeInsert("""
    INSERT INTO transaction_header
      (
        series, date, vch_no, vch_type, sale_type, account_name, debtors, address_1, address_2, address_3, address_4, tin_number, material_centre,
        narration, out_of_state, against_form, net_amount, imported, create_date, hk_ref_no)

        VALUES (${series}, ${date}, ${vch_no}, ${vch_type}, ${sale_type}, ${account_name}, ${debtors}, ${address_1}, ${address_2}, ${city}, ${state}, ${tin_number}, ${material_centre},
        ${narration}, ${out_of_state}, ${against_form}, ${net_amount}, ${imported_flag}, NOW(), ${shippingOrderId}
      )
     """)
       Long vch_code=keys[0][0];
       transactionBodyForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
       transactionFooterForSalesGenerator(vch_code, accountingInvoice.shipping_order_id);
    }
      catch (Exception e) {
            logger.debug("Unable to insert in  transaction header: ",e);
          }
    }
  }

    public void transactionBodyForSalesGenerator(Long vch_code, Long shipping_order_id) {
      int s_no = 0;
      sql.eachRow("""
                      select li.id, li.sku_id, li.qty, li.marked_price, li.hk_price, li.discount_on_hk_price, li.reward_point_discount,
                      t.value as tax_value,li.order_level_discount
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
      Double discount = (invoiceItems.discount_on_hk_price/qty + invoiceItems.order_level_discount/qty + invoiceItems.reward_point_discount/qty);
      Double rate = invoiceItems.hk_price - discount;
      Double vat = invoiceItems.tax_value;
      Double amount = rate*qty;
      s_no++;
      try{
        busySql.executeInsert("""
        INSERT INTO transaction_body
          (
            vch_code, s_no, item_code, qty, unit, mrp, rate, discount, vat, amount, create_date, hk_ref_no
          )

          VALUES (${vch_code}, ${s_no}, ${item_code}, ${qty}, ${unit}, ${mrp}, ${rate}, ${discount}, ${vat}, ${amount}, NOW(), ${lineItemId}
          )
         """)
      }
      catch (Exception e) {
            logger.debug("Unable to insert in  transaction body: ",e);
          }
    }
  }


  public void transactionFooterForSalesGenerator(Long vch_code, Long shipping_order_id) {
    sql.eachRow("""
                    select sum(shipping_charge) as shipping_charge, sum(cod_charge) as cod_charge
                    from line_item li
                    inner join tax t on li.tax_id = t.id
                    where li.shipping_order_id = ${shipping_order_id}
                    group by shipping_order_id
                 """) {
      footerItems ->

    Double shipping_charge = footerItems.shipping_charge;
    Double cod_charge = footerItems.cod_charge;
    try{
      busySql.executeInsert("""
      INSERT INTO transaction_footer
        (
          vch_code, s_no, type, bill_sundry_name, percent, amount, create_date
        )

        VALUES (${vch_code}, 1, 0, 'shipping charge',0 , ${shipping_charge}, NOW()
        )
       """)


      busySql.executeInsert("""
      INSERT INTO transaction_footer
        (
          vch_code, s_no, type, bill_sundry_name, percent, amount, create_date
        )

        VALUES (${vch_code}, 2, 0, 'cod charge',0 , ${cod_charge}, NOW()
        )
       """)
    }
    catch (Exception e) {
            logger.debug("Unable to insert in  transaction footer: ", e);
          }
    }
}
}