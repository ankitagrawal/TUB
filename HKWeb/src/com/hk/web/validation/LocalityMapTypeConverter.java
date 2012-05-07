package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.LocalityMap;
import com.hk.pact.dao.BaseDao;

@Component
public class LocalityMapTypeConverter implements TypeConverter<LocalityMap> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;

  
  public LocalityMap convert(String id, Class<? extends LocalityMap> aClass, Collection<ValidationError> validationErrors) {
    //LocalityMapDao localityMapDao = ServiceLocatorFactory.getService(LocalityMapDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(LocalityMap.class, idLong);
      //return localityMapDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}

