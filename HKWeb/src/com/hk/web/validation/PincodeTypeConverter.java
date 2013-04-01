package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.Pincode;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PincodeTypeConverter implements TypeConverter<Pincode> {

    public void setLocale(Locale locale) {
  }

    @Autowired
    private BaseDao baseDao;

    public Pincode convert(String id, Class<? extends Pincode> aClass, Collection<ValidationError> validationErrors) {
        if (id == null) {
            return null;
        } else {
            return getBaseDao().get(Pincode.class, id);
            // return categoryDao.getCategoryByName(id);
        }
    }

    public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }


}