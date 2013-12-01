package com.hk.admin.util.user

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 19/01/13
 * Time: 9:09 AM
 * To change this template use File | Settings | File Templates.
 */

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import com.hk.domain.user.UserDetail
import com.akube.framework.util.StringUtils
import com.hk.pact.service.user.UserDetailService
import com.hk.util.TokenUtils

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/19/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
class PopulateUnsubscribeToken {
    private String hostName;
    private String dbName;
    private String serverUser;
    private String serverPassword;
    Sql sql;
    PopulateUnsubscribeToken(String hostName, String dbName, String serverUser, String serverPassword){
        this.hostName = hostName;
        this.dbName = dbName;
        this.serverUser = serverUser;
        this.serverPassword = serverPassword;

        sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
                serverPassword, "com.mysql.jdbc.Driver");
    }
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PopulateUnsubscribeToken.class);

    public void populateItemData() {
        String lastUpdateDate;
        Long phone;
        int cnt = 0;
        sql.eachRow("""
                    select * from user u join user_has_role ur on u.id = ur.user_id where ur.role_name IN('HK_USER','HKUNVERIFIED');
                      """){
            userRec ->
            String unsbuscribeToken = TokenUtils.getTokenToUnsubscribeWommEmail(userRec.login);
            try{
            sql.executeUpdate("""
                    UPDATE user SET unsubscribe_token = ${unsbuscribeToken} WHERE login = ${userRec.login};
                   """);
            }catch(Exception ex){
                logger.error("Unable to update unsubscribe_token for user " + userRec.login);
            }
        }
    }
}

