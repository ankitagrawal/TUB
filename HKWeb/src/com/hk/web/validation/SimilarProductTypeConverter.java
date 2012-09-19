package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.product.SimilarProduct;
import com.hk.pact.dao.BaseDao;

/**
 * User: rahul
 * Time: 7 Dec, 2009 12:00:23 PM
 */
@Component
public class SimilarProductTypeConverter implements TypeConverter<SimilarProduct> {
  public void setLocale(Locale locale) {
    //do nothing
  }

  @Autowired
  BaseDao baseDao;

  public SimilarProduct convert(String s, Class<? extends SimilarProduct> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return baseDao.get(SimilarProduct.class, idLong);
    }
  }


}