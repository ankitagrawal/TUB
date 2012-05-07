package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.payment.PaymentHistory;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PaymentHistoryTypeConverter implements TypeConverter<PaymentHistory> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;

  
  public PaymentHistory convert(String id, Class<? extends PaymentHistory> aClass, Collection<ValidationError> validationErrors) {
   // PaymentHistoryDao paymentHistoryDao = ServiceLocatorFactory.getService(PaymentHistoryDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PaymentHistory.class, idLong);
      //return paymentHistoryDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
