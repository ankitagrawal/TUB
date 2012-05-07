package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.Offer;
import com.hk.pact.dao.BaseDao;

@Component
public class OfferTypeConverter implements TypeConverter<Offer> {

  public void setLocale(Locale locale) {
  }

   //OfferDao offerDao;
  @Autowired
  private BaseDao baseDao;


  public Offer convert(String s, Class<? extends Offer> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Offer.class, idLong);
      //return offerDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
