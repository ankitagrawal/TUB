package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.core.OrderStatus;

/**
 * Generated
 */
@Component
public class OrderStatusTypeConverter implements TypeConverter<OrderStatus> {

    @Autowired
    private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

//   OrderStatusDao orderStatusDao;

  public OrderStatus convert(String id, Class<? extends OrderStatus> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(OrderStatus.class, idLong);
      //return orderStatusDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
