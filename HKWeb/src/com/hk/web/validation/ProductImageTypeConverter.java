package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.ProductImage;
import com.hk.pact.dao.BaseDao;

@Component
public class ProductImageTypeConverter implements TypeConverter<ProductImage> {
  public void setLocale(Locale locale) {
  }

  
  @Autowired
  private BaseDao baseDao;
  
  //ProductImageDao productImageDao;

  public ProductImage convert(String s, Class<? extends ProductImage> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ProductImage.class, idLong);
     // return productImageDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
