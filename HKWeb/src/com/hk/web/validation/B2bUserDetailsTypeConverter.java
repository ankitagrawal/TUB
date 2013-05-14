package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.user.B2bUser;
import com.hk.pact.dao.BaseDao;

@Component
public class B2bUserDetailsTypeConverter implements TypeConverter<B2bUser> {
  public void setLocale(Locale locale) {
  }

  
  //B2bUserDetailsDao userDao;
  @Autowired
  private BaseDao baseDao;


  public B2bUser convert(String s, Class<? extends B2bUser> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(B2bUser.class, idLong);
     // return userDao.getUserById(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}