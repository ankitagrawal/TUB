package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.affiliate.Affiliate;
import com.hk.pact.dao.BaseDao;

@Component
public class AffiliateTypeConverter implements TypeConverter<Affiliate> {
   public void setLocale(Locale locale) {
  }

  
  //AffiliateDao affiliateDao;
   @Autowired
   private BaseDao baseDao;


  public Affiliate convert(String id, Class<? extends Affiliate> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Affiliate.class, idLong);
     // return affiliateDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    
}
