package com.hk.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.courier.ReversePickup;

import java.util.Collection;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 5, 2012
 * Time: 4:45:26 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReversePickupTypeConverter implements TypeConverter<ReversePickup> {
  public void setLocale(Locale locale) {
    //do nothing
  }

  @Autowired
  BaseDao baseDao;

  public ReversePickup convert(String s, Class<? extends ReversePickup> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return baseDao.get(ReversePickup.class, idLong);
    }
  }


}
