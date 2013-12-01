package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.ProductVariantServiceType;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: USER
 * Date: Oct 20, 2011
 * Time: 12:24:04 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ServiceTypeTypeConverter implements TypeConverter<ProductVariantServiceType> {

  public void setLocale(Locale locale) {
    // nothing
  }
  
  @Autowired
  private BaseDao baseDao;

   //ProductVariantServiceTypeDao serviceTypeDao;

  public ProductVariantServiceType convert(String id, Class<? extends ProductVariantServiceType> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      //return serviceTypeDao.find(idLong);
      return getBaseDao().get(ProductVariantServiceType.class, idLong);
    }

  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}
