package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.shippingOrder.LineItem;

@Component
public class LineItemTypeConverter implements TypeConverter<LineItem> {
  public void setLocale(Locale locale) {
  }

  
  //LineItemDao lineItemDao;
  @Autowired
  private BaseDao baseDao;


  public LineItem convert(String s, Class<? extends LineItem> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(LineItem.class, idLong);
      //return lineItemDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
  
}
