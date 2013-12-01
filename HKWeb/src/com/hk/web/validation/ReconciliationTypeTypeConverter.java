package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.rv.ReconciliationType;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class ReconciliationTypeTypeConverter implements TypeConverter<ReconciliationType> {

    
    @Autowired
    private BaseDao baseDao;
    
    
  public void setLocale(Locale locale) {
    // nothing
  }

  public ReconciliationType convert(String id, Class<? extends ReconciliationType> aClass, Collection<ValidationError> validationErrors) {
  //  ReconciliationTypeDao reconciliationTypeDao = ServiceLocatorFactory.getService(ReconciliationTypeDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ReconciliationType.class, idLong);
      //return reconciliationTypeDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
