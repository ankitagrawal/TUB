package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.po.PurchaseInvoice;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PurchaseInvoiceTypeConverter implements TypeConverter<PurchaseInvoice> {

    @Autowired
    private BaseDao baseDao;
  public void setLocale(Locale locale) {
    // nothing
  }

  public PurchaseInvoice convert(String id, Class<? extends PurchaseInvoice> aClass, Collection<ValidationError> validationErrors) {
    //PurchaseInvoiceDao purchaseInvoiceDao = ServiceLocatorFactory.getService(PurchaseInvoiceDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(PurchaseInvoice.class, idLong);
      //return purchaseInvoiceDao.find(idLong);
    }

  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
