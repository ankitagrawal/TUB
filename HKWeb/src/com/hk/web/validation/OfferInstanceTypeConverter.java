package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.OfferInstance;
import com.hk.pact.dao.BaseDao;

@Component
public class OfferInstanceTypeConverter implements TypeConverter<OfferInstance> {
  public void setLocale(Locale locale) {
  }

   //OfferInstanceDao offerInstanceDao;
  
  @Autowired
  private BaseDao baseDao;


  public OfferInstance convert(String s, Class<? extends OfferInstance> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(OfferInstance.class, idLong);
      //return offerInstanceDao.find(idLong);
    }
  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
