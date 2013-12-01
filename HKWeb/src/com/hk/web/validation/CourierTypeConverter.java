package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.domain.courier.Courier;
import com.hk.pact.dao.BaseDao;

@Component
public class CourierTypeConverter implements TypeConverter<Courier> {

    @Autowired
    private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

   CourierDao courierDao;

  public Courier convert(String id, Class<? extends Courier> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Courier.class, idLong);
      //return courierDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}
