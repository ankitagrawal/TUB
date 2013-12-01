package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.rv.RvLineItem;
import com.hk.pact.dao.BaseDao;

@Component
public class RvLineItemTypeConverter implements TypeConverter<RvLineItem> {
  public void setLocale(Locale locale) {
  }

  
  @Autowired
  private BaseDao baseDao;
  
  //RvLineItemDao lineItemDao;

  public RvLineItem convert(String s, Class<? extends RvLineItem> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(RvLineItem.class, idLong);
     // return lineItemDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}