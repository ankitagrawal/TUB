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
  private String dbBusyName;
  Sql sql;
  Sql busySql;
  
  BusyPopulateSupplierData(String hostName, String dbName, String serverUser, String serverPassword, String dbBusyName){
    this.hostName = hostName;
    this.dbName = dbName;
    this.serverUser = serverUser;
    this.serverPassword = serverPassword;
    this.dbBusyName = dbBusyName;

    sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
            serverPassword, "com.mysql.jdbc.Driver");

    busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbBusyName, serverUser,
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
      def query = "INSERT INTO " + dbBusyName + "." + "`supplier` (`id`, `name`, `line1`, `line2`, `city`, `state`, `pincode`, `contact_person`, `contact_number`, `tin_number`, `create_date`, imported, credit_days)\
         SELECT su.`id` , su.`name`, su.`line1`, su.`line2`, su.`city`, su.`state`, su.`pincode`, su.`contact_person`, su.`contact_number`, su.`tin_number`, NOW(), 0, su.credit_days                           \
         FROM supplier su                                                                                                                                                                                       \
         WHERE su.update_date >${lastUpdateDate}                                                                                                                                                                \
         ON DUPLICATE KEY UPDATE id=su.id, name=su.name, line1=su.line1, line2=su.line2, city=su.city, state=su.state, pincode=su.pincode, contact_person=su.contact_person,                                    \
         contact_number=su.contact_number, tin_number=su.tin_number, create_date=NOW(), imported=0, credit_days=su.credit_days";

         logger.debug("query formed = "+query);
      //sql.executeInsert(query);
    }
      catch (Exception e) {
            logger.info("Unable to insert in  supplier: ", e);
          }
  }  
}
