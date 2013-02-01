package com.hk.helper;

import java.util.Calendar;
import java.util.Date;

import com.hk.util.HKDateUtil;

public class OrderDateUtil {

    

    public static Date getTargetDispatchDateForWH(Date refDate, Long dispatchDaysOnOrder) {
        int daysForWHProcessing = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(refDate);

        int hour = calendar.get(Calendar.HOUR);

        boolean isPM = calendar.get(Calendar.AM_PM) == Calendar.PM;

        if (isPM && hour != 0) {
            hour += 12; // if this beyond 12:00 noon
        } else if (isPM && hour == 0) {
            hour = 12; // if this is noon
        }

        if (hour < 16) {
            daysForWHProcessing = Integer.parseInt(dispatchDaysOnOrder.toString()) - 1;
        } else {
            daysForWHProcessing = Integer.parseInt(dispatchDaysOnOrder.toString());
        }
        return HKDateUtil.addToDate(refDate, Calendar.DAY_OF_MONTH, daysForWHProcessing);
    }

}
