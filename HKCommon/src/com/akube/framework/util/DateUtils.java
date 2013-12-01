package com.akube.framework.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {
    private static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    public static Date getStartOfDay(Date date) {
        if (date == null) return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.toDateMidnight().getMillis());
    }

    public static Date getEndOfDay(Date date) {
        if (date == null) return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.plusDays(1).toDateMidnight().toDateTime().minusMillis(1).getMillis());
    }

    public static Date getStartOfNextDay(Date date) {
        if (date == null) return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.plusDays(1).toDateMidnight().getMillis());
    }

    public static String getDateString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM, dd yyyy");
        return dateFormat.format(date);
    }

    public static Date getEndOfNextDay(Date date) {
        if (date == null) return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.plusDays(2).toDateMidnight().toDateTime().minusMillis(1).getMillis());
    }

    public static Date getStartOfPreviousDay(Date date) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.minusDays(1).toDateMidnight().toDateTime().getMillis());
    }

    public static Date getDateMinusDays(int days) {
        DateTime dateTime = new DateTime();
        return new Timestamp(dateTime.minusDays(days).toDateTime().getMillis());
    }

    public static Date getEndOfPreviousDay(Date date) {
        if (date == null) {
            return null;
        }
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.toDateMidnight().toDateTime().minusMillis(1).getMillis());
    }

    public static Date getStartOfPreviousSixHours(Date date) {
        if (date == null)
            return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.minusHours(6).toDateTime().getMillis());
    }

    public static Date getStartOfPreviousYear(Date date) {
        if (date == null)
            return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.minusYears(1).toDateTime().getMillis());
    }

    public static Date getEndOfPreviousSixHours(Date date) {
        if (date == null)
            return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.minusMillis(1).toDateTime().getMillis());
    }


    public static Date getDateWithTime(Date date, String time) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        return getDate(dateString, time);
    }

    public static Date getStartOfThisDay(Date date) {
        if (date == null)
            return null;
        DateTime dateTime = new DateTime(date);
        return new Timestamp(dateTime.toDateMidnight().toDateTime().getMillis());
    }

    public static Date getDate(String date, String time) {
        String datetime = date.trim() + " " + getProperTimeString(time.trim());
        Date d = null;
        try {
            d = dateFormat.parse(datetime);
        } catch (ParseException e) {
            logger.error("ParseException in trying to construct timestamp from date: " + date + " and time: " + time);
        }
        return (d == null) ? null : new Timestamp(d.getTime());
    }

    private static String getProperTimeString(String timeString) {
        return timeString.replaceAll(timeRegex, "$1 $3");
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String convertMilliSecondsToDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);

        return formatter.format(calendar.getTime());
    }

    //todo test this
    public DateTime convertStringToDateTime(String dateString) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern("MM/dd/yy HH:mm");
        return formatter.parseDateTime(dateString);
    }

    public static DateTime startOfMonthBack(DateTime currentDate) {
        return currentDate.minusMonths(1);
    }

    /*
    * very specific to query transaction List from ICICI and citrus gateway
    * input gateways want in YYYYMMDD format
    * */
    public static String getStringFromDate(Date date) {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = formatter.format(date);
        return formattedDate.replace("-", "");
    }


    public static void main(String[] args) {
        System.out.println(getStringFromDate(BaseUtils.getCurrentTimestamp()));
    }

    public static final String timeRegex = "([0-9][0-9]?:[0-5][0-9])( *)(AM|PM|am|pm|aM|Am|Pm|pM)";
    public static final String pincodeRegex = "[0-9]{6}";

}
