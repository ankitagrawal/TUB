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


     try{
      sql.executeInsert("""
        INSERT INTO `healthkart_busy`.`supplier` (`id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`)
            SELECT su.`id` , su.`name`, su.`line1`, su.`line2`, su.`city`, su.`state`, su.`pincode`, su.`contact_person`, su.`contact_number`, su.`tin_number`, NOW(), 0
            FROM supplier su
            WHERE su.update_date >${lastUpdateDate}
            ON DUPLICATE KEY UPDATE id=su.id, name=su.name, line1=su.line1, line2=su.line2, city=su.city, state=su.state, pincode=su.pincode, contact_person=su.contact_person,
            contact_number=su.contact_number, tin_number=su.tin_number, create_date=NOW(), imported=0 ;
     """);
    }
      catch (Exception e) {
            logger.debug("Unable to insert in  supplier: ", e);
          }
  }  
}
