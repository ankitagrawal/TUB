package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import com.hk.pact.dao.TaxDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.Tax;
import com.hk.pact.dao.BaseDao;

@Component
public class TaxTypeConverter implements TypeConverter<Tax> {

  
  //TaxDao taxDao;

  public void setLocale(Locale locale) {
  }
  
  @Autowired
  private TaxDao taxDao;

  public Tax convert(String s, Class<? extends Tax> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
//      return taxDao.find(idLong);
      return getTaxDao().get(Tax.class, idLong);
    }
  }

  public TaxDao getTaxDao() {
    return taxDao;
  }
}

