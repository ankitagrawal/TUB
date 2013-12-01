package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.faq.Faq;
import com.hk.pact.dao.faq.FaqDao;

/**
 * User: rahul
 * Time: 7 Dec, 2009 12:00:23 PM
 */
@Component
public class FaqTypeConverter implements TypeConverter<Faq> {
  public void setLocale(Locale locale) {
    //do nothing
  }

  @Autowired
  FaqDao faqDao;

  public Faq convert(String s, Class<? extends Faq> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return faqDao.get(Faq.class, idLong);
    }
  }


}