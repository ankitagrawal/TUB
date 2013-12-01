package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.payment.Payment;
import com.hk.pact.dao.BaseDao;

@Component
public class PaymentTypeConverter implements TypeConverter<Payment> {
  public void setLocale(Locale locale) {
  }

  
  //PaymentDao paymentDao;
  @Autowired
  private BaseDao baseDao;

  public Payment convert(String s, Class<? extends Payment> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Payment.class, idLong);
      //return paymentDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
  
}
