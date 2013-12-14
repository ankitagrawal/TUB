package com.hk.admin.util.finance.busy

import groovy.sql.Sql
import org.slf4j.LoggerFactory

/**
 * Created with IntelliJ IDEA.
 * User: arjun
 * Date: 25/11/13
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
class BusyPopulateReport {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(BusyPopulateReport.class);

    private String hostName;
    private String serverUser;
    private String serverPassword;
    private String dbBusyName;

    Sql busySql;

    BusyPopulateReport(String hostName, String serverUser, String serverPassword, String dbBusyName){
        this.hostName = hostName;
        this.serverUser = serverUser;
        this.serverPassword = serverPassword;
        this.dbBusyName = dbBusyName;

        busySql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbBusyName, serverUser,
                serverPassword, "com.mysql.jdbc.Driver");
    }

    public long recordRetailSalesCount()
    {
        Long retailSalesCount = 0l;

        busySql.eachRow("""
                    select count(*) as sales_count
                    from transaction_header
                    where vch_type = 9
                    and vch_no like '%R%';
                      """){
            latestcount ->
                retailSalesCount = latestcount.sales_count;
        }

        return retailSalesCount;
    }

    public long recordServiceSalesCount()
    {
        Long serviceSalesCount = 0l;

        busySql.eachRow("""
                    select count(*) as sales_count
                    from transaction_header
                    where vch_type = 9
                    and vch_no like '%S%';
                      """){
            latestcount ->
                serviceSalesCount = latestcount.sales_count;
        }

        return serviceSalesCount;
    }

    public long recordB2BSalesCount()
    {
        Long b2bSalesCount = 0l;

        busySql.eachRow("""
                    select count(*) as sales_count
                    from transaction_header
                    where vch_type = 9
                    and vch_no like '%T%';
                      """){
            latestcount ->
                b2bSalesCount = latestcount.sales_count;
        }

        return b2bSalesCount;
    }

    public long recordRtoCount()
    {
        Long rtoCount = 0l;

        busySql.eachRow("""
                    select count(*) as rto_count
                    from transaction_header
                    where vch_type = 3;
                      """){
            latestcount ->
                rtoCount = latestcount.rto_count;
        }

        return rtoCount;
    }

    public long recordPiCount()
    {
        Long piCount = 0l;

        busySql.eachRow("""
                    select count(*) as pi_count
                    from transaction_header
                    where vch_type = 2;
                      """){
            latestcount ->
                piCount = latestcount.pi_count;
        }

        return piCount;
    }

    public long recordPiReturnCount()
    {
        Long piReturnCount = 0l;

        busySql.eachRow("""
                    select count(*) as pi_return_count
                    from transaction_header
                    where vch_type = 10;
                      """){
            latestcount ->
                piReturnCount = latestcount.pi_return_count;
        }

        return piReturnCount;
    }
}