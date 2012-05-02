package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.inventory.po.PurchaseOrder;

/**
 * Generated
 */
@Component
public class PurchaseOrderTypeConverter implements TypeConverter<PurchaseOrder> {

    
    @Autowired
    private BaseDao baseDao;
    
  public void setLocale(Locale locale) {
    // nothing
  }

  public PurchaseOrder convert(String id, Class<? extends PurchaseOrder> aClass, Collection<ValidationError> validationErrors) {
    //PurchaseOrderDao purchaseOrderDao = ServiceLocatorFactory.getService(PurchaseOrderDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PurchaseOrder.class, idLong);
      //return purchaseOrderDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
