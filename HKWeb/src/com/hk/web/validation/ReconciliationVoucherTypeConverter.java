package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.rv.ReconciliationVoucher;
import com.hk.pact.dao.BaseDao;

@Component
public class ReconciliationVoucherTypeConverter implements TypeConverter<ReconciliationVoucher> {

    @Autowired
    private BaseDao baseDao;
    
  public void setLocale(Locale locale) {
    // nothing
  }

  public ReconciliationVoucher convert(String id, Class<? extends ReconciliationVoucher> aClass, Collection<ValidationError> validationErrors) {
    //ReconciliationVoucherDao reconciliationVoucherDao = ServiceLocatorFactory.getService(ReconciliationVoucherDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ReconciliationVoucher.class, idLong);
      //return reconciliationVoucherDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}