package com.akube.framework.util;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/25/12
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringUtils {
    public static String[] getUserPhoneList(String ph){
        String[] phones = null;
        if (ph.contains("-")){
            ph = ph.replace("-","");
        }
        if (ph.contains(",")){
            phones = ph.split(",");
        }else if (ph.contains("/")){
            phones = ph.split("/");
        }else{
            phones = new String[1];
            phones[0] = ph;
        }
        return phones;
    }

    public static long getUserPhone(String userPhone){
        int start = userPhone.length() - 10;
        //consider only the last 10 digits
        String strPh = userPhone.substring(start, userPhone.length());
        long phoneNumber = Long.parseLong(strPh);
        return phoneNumber;
    }
}
