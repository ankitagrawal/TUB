package com.hk.admin.util.finance.busy
import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: Tarun
 * Date: April 13, 2012
 * Time: 01:28:07 PM
 * To change this template use File | Settings | File Templates.
 */


public class BusyPopulateSupplierData {

  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyTableDataGenerator.class);

  static Sql sql = Sql.newInstance("jdbc:mysql://localhost:3306/healthkart_stag", "root",
          "admin2K11!", "com.mysql.jdbc.Driver");

  static Sql busySql = Sql.newInstance("jdbc:mysql://localhost:3306/healthkart_busy", "root",
          "admin2K11!", "com.mysql.jdbc.Driver");

  public static void transactionHeaderForSalesGenerator() {
    String lastUpdateDate;
    busySql.eachRow("""
                    select max(create_date) as max_date
                    from supplier;
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
        INSERT INTO `healthkart_busy`.`supplier` (`id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`)
            SELECT `id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`
            FROM supplier
            WHERE supplier.create_date >${lastUpdateDate} ;
     """);
    }
      catch (Exception e) {
            logger.debug("Unable to insert in  supplier: ", e);
          }
  }

  
  public static void main(String[] args){
    transactionHeaderForSalesGenerator();
  }
}
