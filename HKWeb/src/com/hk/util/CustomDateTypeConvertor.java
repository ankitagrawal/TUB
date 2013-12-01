package com.hk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Created by IntelliJ IDEA.
 * User: Ajeet
 * Date: Sep 7, 2011
 * Time: 8:07:19 PM
 * To change this template use File | Settings | File Templates.
 */

public class CustomDateTypeConvertor implements TypeConverter<Date> {

  private String customDateFormat = "yyyy-MM-dd HH:mm";

  private SimpleDateFormat sdf = new SimpleDateFormat(customDateFormat);

  public void setLocale(Locale locale) {    
  }

  public Date convert(String s, Class<? extends Date> aClass, Collection<ValidationError> validationErrors) {
    Date date = new Date();
    try {
      date = sdf.parse(s);
    } catch (ParseException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return date;
  }
}
