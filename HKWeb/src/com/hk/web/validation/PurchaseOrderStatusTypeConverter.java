package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PurchaseOrderStatusTypeConverter implements TypeConverter<PurchaseOrderStatus> {

    
    @Autowired
    private BaseDao baseDao;
    
  public void setLocale(Locale locale) {
    // nothing
  }

  public PurchaseOrderStatus convert(String id, Class<? extends PurchaseOrderStatus> aClass, Collection<ValidationError> validationErrors) {
    //PurchaseOrderStatusDao purchaseOrderStatusDao = ServiceLocatorFactory.getService(PurchaseOrderStatusDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PurchaseOrderStatus.class, idLong);
     // return purchaseOrderStatusDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
