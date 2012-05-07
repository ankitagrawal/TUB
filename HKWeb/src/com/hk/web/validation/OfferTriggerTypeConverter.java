package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.OfferTrigger;
import com.hk.pact.dao.BaseDao;

@Component
public class OfferTriggerTypeConverter implements TypeConverter<OfferTrigger> {
  public void setLocale(Locale locale) {
  }

  // OfferTriggerDao offerTriggerDao;
  
  @Autowired
  private BaseDao baseDao;


  public OfferTrigger convert(String s, Class<? extends OfferTrigger> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(OfferTrigger.class, idLong);
      //return offerTriggerDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
