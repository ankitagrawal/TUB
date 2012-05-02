package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.catalog.Manufacturer;

@Component
public class ManufacturerTypeConverter implements TypeConverter<Manufacturer> {
  
  //ManufacturerDao manufacturerDao;
    
    @Autowired
    private BaseDao baseDao;


  public void setLocale(Locale locale) {

  }

  public Manufacturer convert(String s, Class<? extends Manufacturer> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Manufacturer.class, idLong);
      //return manufacturerDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
