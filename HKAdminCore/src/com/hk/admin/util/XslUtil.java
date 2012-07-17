package com.hk.admin.util;

import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;

import net.sourceforge.stripes.action.Resolution;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 14, 2012
 * Time: 3:15:56 PM
 * To change this template use File | Settings | File Templates.
 */
 @Component
public class XslUtil {

   public static Date getDate(String value) {
    Date date = null;
    try {
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM");
      date = (Date) formatter.parse(value);
    } catch (Exception e) {

    }
    return date;
  }

  public static Double getDouble(String value) {
    Double valueInDouble = null;
    try {
      valueInDouble = Double.parseDouble(value);
    } catch (Exception e) {

    }
    return valueInDouble;
  }

  public static Long getLong(String value) {
    Long valueInLong = null;
    try {
      valueInLong = Long.parseLong(value.replace(".0", ""));
    } catch (Exception e) {

    }
    return valueInLong;
  }

}
