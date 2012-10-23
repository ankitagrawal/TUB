package com.hk.admin.util.drishti

import groovy.sql.Sql
import org.slf4j.LoggerFactory
import com.hk.domain.user.UserDetail

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/19/12
 * Time: 5:35 PM
 * To change this template use File | Settings | File Templates.
 */
class PopulateUserDetail {
    private String hostName;
    private String dbName;
    private String serverUser;
    private String serverPassword;
    Sql sql;
    PopulateUserDetail(String hostName, String dbName, String serverUser, String serverPassword){
        this.hostName = hostName;
        this.dbName = dbName;
        this.serverUser = serverUser;
        this.serverPassword = serverPassword;

        sql = Sql.newInstance("jdbc:mysql://"+hostName+":3306/"+dbName, serverUser,
                serverPassword, "com.mysql.jdbc.Driver");
    }
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(PopulateUserDetail.class);

    public void populateItemData() {
        String lastUpdateDate;
        Long phone;
        int cnt = 0;
        sql.eachRow("""
                    select u.id, a.phone, kp.karma_points from user u join address a on u.id = a.user_id
                    join user_karma_profile kp on u.id = kp.user_id;
                      """){
            userDetails ->
            String[] phones = null;
            String ph = userDetails.phone;
            if (ph.contains('-')){
                ph = ph.replace('-','');
            }
            if (ph.contains(",")){
                phones = ph.split(",");
            }else if (ph.contains("/")){
                phones = ph.split("/");
            }else{
                phones = new String[1];
                phones[0] = ph;
            }

            for (String userPhone : phones){
              try{
                    cnt++;
                    UserDetail userDetail = new UserDetail();
                    int start = userPhone.length() - 10;
                    //consider only the last 10 digits
                    String strPh = userPhone.substring(start, userPhone.length() - 1);
                    long phoneNumber = Long.parseLong(strPh);
                    long id = userDetails.id;
                    int priority = userDetails.karma_points > 300 ? 1 : 0;
                  sql.executeInsert("""
                    INSERT INTO user_detail values (${cnt}, ${phoneNumber}, ${id}, ${priority})
                   """);
                  }catch (Exception ex){

                  }
              }
            }
        }
}
