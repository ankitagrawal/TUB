package com.akube.framework.util;

import java.util.LinkedList;
import java.util.List;

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

    /**
     * @param s         delimiter separated string
     * @param delimiter default delimiter value is ','
     * @return the values are trimmed and the empty values are removed
     */
    public static List<String> getListFromString(String s, String delimiter) {
        if (s == null) return null;
        delimiter = (delimiter == null || delimiter.isEmpty()) ? "," : delimiter;
        List<String> retList = new LinkedList<String>();
        String[] ss = s.split(delimiter);
        for (String str : ss) {
            if (str != null && !str.isEmpty()) {
                str = str.trim();
                retList.add(str);
            }
        }
        return retList;
    }
}
