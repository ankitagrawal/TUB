package com.hk.admin.util;

import java.util.List;
import java.util.HashSet;


public class HKDeliveryUtil {

    // Getting comma seperated string for the duplicated
    public static String convertListToString(List<String> list) {
        StringBuffer strBuffr = new StringBuffer();
        for (String string : new HashSet<String>(list) ) {
            strBuffr.append(string);
            strBuffr.append(",");
        }
        return strBuffr.toString();
    }
}
