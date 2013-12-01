package com.hk.util;

import java.util.Calendar;
import java.util.Date;

/**
 * @author vaibhav.adlakha
 */
public class HKDateUtil {

    public static Date getNow() {
        return new Date();
    }

    /**
     * @param date
     * @param type one of the fields from Calendar, e.g. Calendar.DAY_OF_MONTH
     * @param noOfUnits
     * @return a new <code>Date</code> object with the noOfUnits of type added to the date parameter
     * @exception IllegalArgumentException when type is not valid Calendar field for addition
     */
    public static Date addToDate(Date date, int type, int noOfUnits) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(type, noOfUnits);
        return calendar.getTime();
    }
}
