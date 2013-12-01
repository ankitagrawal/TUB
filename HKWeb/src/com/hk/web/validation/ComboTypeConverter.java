package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.combo.Combo;
import com.hk.pact.dao.BaseDao;

@Component
public class ComboTypeConverter implements TypeConverter<Combo> {
  public void setLocale(Locale locale) {
  }

  
  //ComboDao comboDao;
  @Autowired
  private BaseDao baseDao;



  public Combo convert(String s, Class<? extends Combo> aClass, Collection<ValidationError> validationErrors) {
    if (s == null) {
      return null;
    } else {
        return getBaseDao().get(Combo.class, s);
      //return comboDao.find(s);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}