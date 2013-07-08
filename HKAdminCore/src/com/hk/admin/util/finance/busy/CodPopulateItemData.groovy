package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: Rajesh Kumar
 * Date: 7/4/13
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
class CodPopulateItemData {
    private String hostName;
    private String databaseName;
    private String serverUser;
    private String serverPassword;
    Sql sql;
    Sql busySql;

    CodPopulateItemData(String hostName, String databaseName, String serverUser, String serverPassword) {
        this.hostName = hostName;
        this.databaseName = databaseName;
        this.serverUser = serverUser;
        this.serverPassword = serverPassword;

        sql = Sql.newInstance("jdbc:mysql://" + hostName + ":3306/" + databaseName, serverUser,
                serverPassword, "com.mysql.jdbc.Driver");

    }

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CodPopulateItemData.class);

    public Integer populateCodCalldata() {
        try {
            int rowUpdateKnowlarity = sql.executeUpdate("""
            update user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
           set ucc.call_status=10
           where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>10 and  p.payment_date >(
           SELECT
           case
            when (TIME(NOW()) between '00:00:00' and '08:00:00')
            then CONCAT(DATE((DATE(NOW())-1))," ", '15:00:00' )

           when(TIME(NOW()) between '19:00:00' and '24:00:00')
            then CONCAT(DATE(DATE(NOW())-1)," ", '15:00:00' )

           when(TIME(NOW()) between '08:00:00' and '12:00:00')
           then   CONCAT(DATE(DATE(NOW())-1) , " " ,  TIME(TIME('07:00:00')+TIME(NOW())))

           else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 4 HOUR)))
           end  );


""");
            int rowUpdateEffortBpo = sql.executeUpdate("""
                            update  user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
                            set ucc.call_status=60
                            where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>60 and  p.payment_date between (
                            select
                            TIMESTAMP(case
                             when (TIME(NOW()) between '00:00:00' and '08:00:00')
                             then CONCAT(DATE((DATE(NOW())-1))," ", '08:00:00' )
                            when(TIME(NOW()) between '19:00:00' and '24:00:00')
                             then CONCAT(DATE(DATE(NOW())-1)," ", '08:00:00' )
                            else   CONCAT(DATE(DATE(NOW())-1) , " ", TIME(NOW()))
                            end))
                            and (select TIMESTAMP(case
                             when (TIME(NOW()) between '00:00:00' and '08:00:00')
                             then CONCAT(DATE((DATE(NOW())-1))," ", '15:00:00' )

                            when(TIME(NOW()) between '19:00:00' and '24:00:00')
                             then CONCAT(DATE(DATE(NOW())-1)," ", '15:00:00' )

                            when(TIME(NOW()) between '08:00:00' and '12:00:00')
                            then   CONCAT(DATE(DATE(NOW())-1) , " " , TIME(TIME('07:00:00')+TIME(NOW())))
                            else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 4 HOUR)))
                            end  )) ;
                            """);

            int rowUpdateHK = sql.executeUpdate("""
                  update   user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
                  set ucc.call_status=70
                  where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>70 and  p.payment_date <(
                  select
                  case
                   when (TIME(NOW()) between '00:00:00' and '08:00:00')
                   then CONCAT(DATE((DATE(NOW())-1))," ", '08:00:00' )
                  when(TIME(NOW()) between '19:00:00' and '24:00:00')
                   then CONCAT(DATE(DATE(NOW())-1)," ", '08:00:00' )
                  else   CONCAT(DATE(DATE(NOW())-1) , " ", TIME(NOW()))
                  end) ;

""");
            return (rowUpdateKnowlarity + rowUpdateEffortBpo + rowUpdateHK).toInteger();
        }
        catch (Exception e) {
            Logger.info("Unable to Update in  item: ", e);
        }
    }

}
