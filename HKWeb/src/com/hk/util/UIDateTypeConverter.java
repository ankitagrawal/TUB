package com.hk.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 1:52 PM
 * this is written for subscription calenders actually
 */
public class UIDateTypeConverter implements TypeConverter<Date> {

    private String customDateFormat = "dd/MM/yyyy";

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