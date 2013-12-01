package com.hk.web.validation;

import com.hk.domain.courier.Zone;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/**
 * User: Tarun
 * Time: 6 Dec, 2012 12:00:23 PM
 */
@Component
public class ZoneTypeConverter implements TypeConverter<Zone> {
  public void setLocale(Locale locale) {
    //do nothing
  }

  @Autowired
  BaseDao baseDao;

  public Zone convert(String s, Class<? extends Zone> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return baseDao.get(Zone.class, idLong);
    }
  }


}