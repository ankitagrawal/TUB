package com.hk.web;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 3/13/12
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertEncryptedToNormalDouble implements TypeConverter<Double>{
  public void setLocale(Locale locale) {
  }

  public Double convert(String s, Class<? extends Double> aClass, Collection<ValidationError> validationErrors) {
    Double valueDouble = null;
    try {
      valueDouble = Double.parseDouble(s);
    } catch (NumberFormatException e) {
      // unable to parse.. try to unencrupt and then parse
      try {
        valueDouble = Double.parseDouble(CryptoUtil.decrypt(s));
      } catch (NumberFormatException e1) {
        // unable to parse
      } catch (Exception e2) {
        // maybe cryptoUtil throws exception.. catching that as well
      }
    }
    return valueDouble;
  }
}
