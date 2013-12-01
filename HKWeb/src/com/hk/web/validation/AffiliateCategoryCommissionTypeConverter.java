package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.affiliate.AffiliateCategoryCommission;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class AffiliateCategoryCommissionTypeConverter implements TypeConverter<AffiliateCategoryCommission> {

  public void setLocale(Locale locale) {
    // nothing
  }

   //AffiliateCategoryDao affiliateCategoryCommissionDao;
  @Autowired
  private BaseDao baseDao;


  public AffiliateCategoryCommission convert(String id, Class<? extends AffiliateCategoryCommission> aClass, Collection<ValidationError> validationErrors) {

    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(AffiliateCategoryCommission.class, idLong);
     // return affiliateCategoryCommissionDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}
