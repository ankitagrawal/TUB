package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.GrnStatus;
import com.hk.pact.dao.BaseDao;


@Component
public class GrnStatusTypeConverter implements TypeConverter<GrnStatus> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;

  
  public GrnStatus convert(String id, Class<? extends GrnStatus> aClass, Collection<ValidationError> validationErrors) {
   // GrnStatusDao grnStatusDao = ServiceLocatorFactory.getService(GrnStatusDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(GrnStatus.class, idLong);
     // return grnStatusDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
