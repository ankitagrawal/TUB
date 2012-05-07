package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.pact.dao.BaseDao;

@Component
public class ComboInstanceHasProductVariantTypeConverter implements TypeConverter<ComboInstanceHasProductVariant> {

    
    @Autowired
    private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

  public ComboInstanceHasProductVariant convert(String id, Class<? extends ComboInstanceHasProductVariant> aClass, Collection<ValidationError> validationErrors) {
   // ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao = ServiceLocatorFactory.getService(ComboInstanceHasProductVariantDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ComboInstanceHasProductVariant.class, idLong);
      //return comboInstanceHasProductVariantDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}
