package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.ProductVariantPaymentType;
import com.hk.pact.dao.BaseDao;


@Component
public class PaymentTypeTypeConverter implements TypeConverter<ProductVariantPaymentType> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;
//   ProductVariantPaymentTypeDao paymentTypeDao;

  public ProductVariantPaymentType convert(String id, Class<? extends ProductVariantPaymentType> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ProductVariantPaymentType.class, idLong);
      //return paymentTypeDao.find(idLong);
    }

  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}

