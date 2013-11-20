package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.Logger

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


    public Integer populateCodCalldata() {
        try {
            int rowUpdateKnowlarity = sql.executeUpdate("""
          update user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
          set ucc.call_status=10, ucc.remark='pending with knowlarity'
          where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>10 and  p.payment_date >(
          SELECT
          case
           when (TIME(NOW()) between '00:00:00' and '08:59:59')
           then CONCAT(DATE((DATE(NOW())-1))," ", TIME('19:00:00') )

          when(TIME(NOW()) between '21:00:00' and '23:59:59')
           then CONCAT(DATE(DATE(NOW()))," ", TIME('19:00:00' ))

          when(TIME(NOW()) between '09:00:00' and '10:59:59')
          then   CONCAT(DATE(DATE(NOW())-1) , " " ,  TIME(TIME('21:00:00')-(TIME(NOW())-TIME('09:00:00'))))

          else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 2 HOUR)))
          end   ) ;
""");
            int rowUpdateEffortBpo = sql.executeUpdate("""
                           update  user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
                           set ucc.call_status=60,ucc.remark='pending with EffortBpo'
                           where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>60 and  p.payment_date between
                           (select TIMESTAMP(case
                            when (TIME(NOW()) between '00:00:00' and '08:59:59')
                            then CONCAT(DATE((DATE(NOW())-1))," ", TIME('13:00:00'))

                           when(TIME(NOW()) between '21:00:00' and '23:59:59')
                            then CONCAT(DATE(DATE(NOW()))," ", TIME('13:00:00'))

                           when(TIME(NOW()) between '09:00:00' and '16:59:59')
                           then   CONCAT(DATE(DATE(NOW())-1) , " " , TIME(TIME('21:00:00')-(TIME('08:00:00')-(TIME(NOW())-TIME('09:00:00')))))
                           else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 8 HOUR)))
                           end  ))
                           and
                           (
                           SELECT
                           case
                            when (TIME(NOW()) between '00:00:00' and '08:59:59')
                            then CONCAT(DATE((DATE(NOW())-1))," ", TIME('19:00:00'))

                           when(TIME(NOW()) between '21:00:00' and '23:59:59')
                            then CONCAT(DATE(DATE(NOW()))," ", TIME('19:00:00'))

                           when(TIME(NOW()) between '09:00:00' and '10:59:59')
                           then   CONCAT(DATE(DATE(NOW())-1) , " " ,  TIME(TIME('21:00:00')-(TIME(NOW())-TIME('09:00:00'))))

                           else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 2 HOUR)))
                           end  )  ;
                            """);

            int rowUpdateHK = sql.executeUpdate("""
                  update   user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
                  set ucc.call_status=70,ucc.remark='pending with HealthKart'
                  where bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40  and ucc.call_status<>70 and  p.payment_date <(
                  select TIMESTAMP(case
                   when (TIME(NOW()) between '00:00:00' and '08:59:59')
                   then CONCAT(DATE((DATE(NOW())-1))," ", TIME('13:00:00'))

                  when(TIME(NOW()) between '21:00:00' and '23:59:59')
                   then CONCAT(DATE(DATE(NOW()))," ", TIME('13:00:00'))

                  when(TIME(NOW()) between '09:00:00' and '16:59:59')
                  then   CONCAT(DATE(DATE(NOW())-1) , " " , TIME(TIME('21:00:00')-(TIME('08:00:00')-(TIME(NOW())-TIME('09:00:00')))))
                  else CONCAT(DATE((DATE(NOW())))," ", TIME(DATE_SUB(NOW(),INTERVAL 8 HOUR)))
                  end  )) ;

                """);

            int rowUpdateForStore = sql.executeUpdate("""
                update user_cod_call ucc join base_order bo on ucc.order_id=bo.id join payment p on bo.payment_id=p.id
                set ucc.call_status=70,remark='pending with HealthKart'
                where bo.store_id in (2,3) and ucc.call_status<>70 and  bo.order_status_id in(15,20) and p.payment_status_id=2 and p.payment_mode_id=40;
              """)

            return (rowUpdateKnowlarity + rowUpdateEffortBpo + rowUpdateHK + rowUpdateForStore).toInteger();
        }
        catch (Exception e) {
            Logger.info("Unable to Update in  item: ", e);
        }
    }

}
