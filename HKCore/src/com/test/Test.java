package com.test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.hk.util.HKDateUtil;

public class Test {

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = calendar.getTime();
        try {
            Date d1 = df.parse("2012-05-17 15:59:00");
            System.out.println(d1);
            calendar.setTime(d1);

            int hour = calendar.get(Calendar.HOUR);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            int am = calendar.get(Calendar.AM);
            int pm = calendar.get(Calendar.PM);

            boolean isAM = calendar.get(Calendar.AM_PM) == Calendar.AM;

            Date dateTemp  = HKDateUtil.addToDate(new Date(), Calendar.DAY_OF_MONTH, 10);
            System.out.println( ":" + dateTemp);

            DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
            System.out.println( ":" + dateFormat.format(date));


        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}
