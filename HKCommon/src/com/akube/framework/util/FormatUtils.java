package com.akube.framework.util;


import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: Kani
 * Date: Jun 2, 2009
 */
public class FormatUtils {

	private static Logger logger = LoggerFactory.getLogger(FormatUtils.class);

	public static final String defaultDateFormatPattern = "yyyy-MM-dd HH:mm";
	public static final String currencyFormatPattern = "###,##0.00";
	public static final String uiDateFormatPattern = "dd/MM/yyyy";

	public static final String lineSeperator = System.getProperty("line.separator");

	private static NumberFormat currencyFormat = NumberFormat.getNumberInstance();
	private static NumberFormat decimalFormat = NumberFormat.getNumberInstance();
	private static DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(defaultDateFormatPattern);

  private static final SimpleDateFormat dateFormatForUserEnd = new SimpleDateFormat("MMM dd, yyyy");

  static {
    currencyFormat.setMaximumFractionDigits(2);
    currencyFormat.setMinimumFractionDigits(2);
    currencyFormat.setGroupingUsed(true);

    decimalFormat.setMaximumFractionDigits(2);
    decimalFormat.setMinimumFractionDigits(2);
    decimalFormat.setGroupingUsed(false);
  }

  public static String getCurrencyFormat(Double val) {
    String formattedVal = "";
    try {
      formattedVal = currencyFormat.format(val);
    } catch (NumberFormatException nfe) {
      logger.error("error while formatting value " + val + " as string");
    } catch (IllegalArgumentException iae) {
      logger.error("illegal argument exception error while formatting value " + val + " as string");
    }
    return formattedVal;
  }

  public static String getDecimalFormat(Double val) {
    return decimalFormat.format(val);
  }

  /**
   * Using this method to round off using whatever standard java is using.
   * It is ROUND_HALF_EVEN by default it seems and couldn't find a way to change that
   * <p/>
   * Mysql seems to use the common method. this was causing a really bad bug as follows
   * <p/>
   * Shopping cart grand total after calculation was 42.765
   * Java was rounding this off as 42.76
   * but when we were creating a payment row for the same, it was getting rounded off in mysql as 42.77
   * <p/>
   * when the control came back from the gateway, a payment mismatch error was getting thrown. our
   * solution for now is to round off currency numbers at our end itself in the respective DAO's. this
   * method will be used to round off
   *
   * @param n
   * @return
   */
  public static Double getCurrencyPrecision(Double n) {
    Double parsed = null;
    try {
      String currencyString = currencyFormat.format(n);
      parsed = currencyFormat.parse(currencyString).doubleValue();
    }  catch (ParseException e) {
      logger.error("error parsing " + n + " for currecny precision", e.getMessage());
    } catch (NumberFormatException nfe) {
      logger.error("number format exception " + n + " for currecny precision", nfe.getMessage());
    }
    return parsed ;
  }

  public static String getDefaultFormattedDate(Date date) {
    return dateFormatter.print(new DateTime(date));
  }

  public static String getFormattedDateForUserEnd(Date date) {
    if (date == null) return "";
    return dateFormatForUserEnd.format(date);
  }


  public static void main(String[] args) {
    System.out.println(currencyFormat.format(30.0));
  }
}