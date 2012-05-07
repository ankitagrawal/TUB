package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.payment.PreferredBankGateway;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PreferredBankGatewayTypeConverter implements TypeConverter<PreferredBankGateway> {

    @Autowired
    private BaseDao baseDao;
    
  public void setLocale(Locale locale) {
    // nothing
  }

  public PreferredBankGateway convert(String id, Class<? extends PreferredBankGateway> aClass, Collection<ValidationError> validationErrors) {
    //PreferredBankGatewayDao preferredBankGatewayDao = ServiceLocatorFactory.getService(PreferredBankGatewayDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PreferredBankGateway.class, idLong);
      //return preferredBankGatewayDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
