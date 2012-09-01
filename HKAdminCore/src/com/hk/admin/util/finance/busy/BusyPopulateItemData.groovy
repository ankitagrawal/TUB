package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import org.slf4j.Logger

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class BusyPopulateItemData {

  private String hostName;
  private String dbName;
  private String serverUser;
  private String serverPassword;
  Sql sql;
  Sql busySql;
  BusyPopulateItemData(String hostName, String dbName, String serverUser, String serverPassword){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_busy", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");
  }
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateItemData.class);


  public void populateItemData() {
    String lastUpdateDate;
    busySql.eachRow("""
                    select max(create_date) as max_date
                    from item_detail;
                      """){
          lastDate ->
          lastUpdateDate = lastDate.max_date;
      }
    if(lastUpdateDate == null){
      lastUpdateDate = "2009-01-01";
    }


     try{
      sql.executeInsert("""

INSERT IGNORE INTO healthkart_busy.item_detail(item_code ,item_name ,print_name ,parent_group ,unit,sale_price ,purchase_price ,mrp ,
tax_rate_local,tax_rate_central ,item_description_1 , item_description_2 ,item_description_3 ,item_description_4 ,
 imported,create_date)


SELECT  sk.id  ,
	substring(concat(p.name,ifnull(pv.variant_name,'')),1,40) ,
 	substring(concat(p.name,ifnull(pv.variant_name,'')),41,40) ,p.primary_category ,'Pcs',
	pv.hk_price ,
	sk.cost_price ,
	pv.marked_price ,
       t.value ,t.value ,
    left(if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',1),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',2))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',1),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',2))),40)as item1,

 left(if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',2),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',3))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',2),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',3))),40)as item2,
 left(if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',3),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',4))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',3),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',4))),40)as item3,
left(if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',4),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',5))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',4),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',5))),40) as item4
,0,sk.create_date


FROM sku sk INNER JOIN product_variant pv ON sk.product_variant_id=pv.id
            INNER JOIN product p ON pv.product_id=p.id
            INNER JOIN tax t   ON sk.tax_id=t.id
	    INNER JOIN
	(select pv.id AS product_variant_id,
      group_concat(concat( po.name ,':',po.value ) )as pv_options_concat
      from ((product_variant pv LEFT JOIN
            product_variant_has_product_option pvpo
            on((pv.id = pvpo.product_variant_id))) LEFT JOIN
               product_option po on((po.id = pvpo.product_option_id)))
      group by pv.id)in_view_pv_po_option

	   on in_view_pv_po_option.product_variant_id = pv.id
    WHERE sk.create_date >${lastUpdateDate}
;
     """);
    }
      catch (Exception e) {

        Logger.debug("Unable to insert in  item: ",e);
          }
  }
}
