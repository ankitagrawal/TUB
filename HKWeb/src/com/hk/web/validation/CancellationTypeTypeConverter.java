package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.CancellationType;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class CancellationTypeTypeConverter implements TypeConverter<CancellationType> {

    @Autowired
    private BaseDao baseDao;

    
  public void setLocale(Locale locale) {
    // nothing
  }

  public CancellationType convert(String id, Class<? extends CancellationType> aClass, Collection<ValidationError> validationErrors) {
    //CancellationTypeDao cancellationTypeDao = ServiceLocatorFactory.getService(CancellationTypeDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(CancellationType.class, idLong);
     // return cancellationTypeDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}
