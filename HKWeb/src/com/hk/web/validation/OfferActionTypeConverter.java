package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.OfferAction;
import com.hk.pact.dao.BaseDao;

@Component
public class OfferActionTypeConverter implements TypeConverter<OfferAction>{
  public void setLocale(Locale locale) {
  }

  @Autowired
  private BaseDao baseDao;

 //  OfferActionDao offerActionDao;

  public OfferAction convert(String s, Class<? extends OfferAction> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(OfferAction.class, idLong);
      //return offerActionDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
