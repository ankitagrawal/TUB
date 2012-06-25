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


public class BusyPopulateItemData {

  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyTableDataGenerator.class);

  static Sql sql = Sql.newInstance("jdbc:mysql://localhost:3306/healthkart_stag", "root",
          "admin2K11!", "com.mysql.jdbc.Driver");

  static Sql busySql = Sql.newInstance("jdbc:mysql://localhost:3306/healthkart_busy", "root",
          "admin2K11!", "com.mysql.jdbc.Driver");

  public static void transactionHeaderForSalesGenerator() {
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

    System.out.println("Inserting...");
    

     try{
      sql.executeInsert("""

INSERT INTO healthkart_busy.item_detail(item_code ,item_name ,print_name ,parent_group ,unit,sale_price ,purchase_price ,mrp ,
tax_rate_local,tax_rate_central ,item_description_1 , item_description_2 ,item_description_3 ,item_description_4 ,
 imported,create_date)


SELECT  sk.id  ,
	substring(concat(p.name,ifnull(pv.variant_name,'')),1,40) ,
 	substring(concat(p.name,ifnull(pv.variant_name,'')),40,80) ,p.primary_category ,'Pcs',
	pv.hk_price ,
	sk.cost_price ,
	pv.marked_price ,
       t.value ,t.value ,
	substring_index(in_view_pv_po_option.pv_options_concat,',',1) as item1,
 if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',1),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',2))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',1),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',2)))as item2,
 if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',2),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',3))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',2),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',3)))as item3,
if (trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',3),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',4))= in_view_pv_po_option.pv_options_concat,NULL,
trim(leading  concat(substring_index(in_view_pv_po_option.pv_options_concat,',',3),',')FROM substring_index(in_view_pv_po_option.pv_options_concat,',',4))) as item4
,0,p.create_date


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
            logger.debug("Unable to insert in  item: ",e);
          }
  }

  
  public static void main(String[] args){
    transactionHeaderForSalesGenerator();
  }
}
