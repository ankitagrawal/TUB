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
  private String hostName;
  private String dbName;
  private String serverUser;
  private String serverPassword;
  Sql sql;
  Sql busySql;
  
  BusyPopulateSupplierData(String hostName, String dbName, String serverUser, String serverPassword){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/healthkart_busy", serverUser,
            serverPassword, "com.mysql.jdbc.Driver");
  }
  private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateSupplierData.class);

  public void busySupplierUpdate() {
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
        INSERT IGNORE INTO `healthkart_busy`.`supplier` (`id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`)
            SELECT `id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`
            FROM supplier
            WHERE supplier.create_date >${lastUpdateDate} ;
     """);
    }
      catch (Exception e) {
            logger.debug("Unable to insert in  supplier: ", e);
          }
  }  
}
