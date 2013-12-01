package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.pact.dao.BaseDao;

@Component
public class ComboInstanceTypeConverter implements TypeConverter<ComboInstance> {

    @Autowired
    private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

  public ComboInstance convert(String id, Class<? extends ComboInstance> aClass, Collection<ValidationError> validationErrors) {
    //ComboInstanceDao comboInstanceDao = ServiceLocatorFactory.getService(ComboInstanceDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ComboInstance.class, idLong);
      //return comboInstanceDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}
