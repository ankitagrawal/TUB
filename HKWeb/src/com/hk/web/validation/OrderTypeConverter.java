package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.order.Order;
import com.hk.pact.dao.BaseDao;

/**
 * User: rahul
 * Time: 7 Dec, 2009 12:00:23 PM
 */
@Component
public class OrderTypeConverter implements TypeConverter<Order> {
  public void setLocale(Locale locale) {
    //do nothing
  }

  @Autowired
  private BaseDao baseDao;

  
  //OrderDao orderDao;

  public Order convert(String s, Class<? extends Order> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Order.class, idLong);
     // return orderDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
