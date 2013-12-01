package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.PaymentMode;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PaymentModeTypeConverter implements TypeConverter<PaymentMode> {

  public void setLocale(Locale locale) {
    // nothing
  }

  
  //PaymentModeDao paymentModeDao;
  
  @Autowired
  private BaseDao baseDao;
  
  public PaymentMode convert(String id, Class<? extends PaymentMode> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PaymentMode.class, idLong);
      //return paymentModeDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
