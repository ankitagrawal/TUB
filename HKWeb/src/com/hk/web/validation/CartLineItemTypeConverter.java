package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.order.CartLineItem;
import com.hk.pact.dao.BaseDao;

@Component
public class CartLineItemTypeConverter implements TypeConverter<CartLineItem> {

  private static Logger logger = LoggerFactory.getLogger(CartLineItemTypeConverter.class);

  public void setLocale(Locale locale) {
  }

  
  @Autowired
  private BaseDao baseDao;


  public CartLineItem convert(String s, Class<? extends CartLineItem> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
      logger.error("cart line item could not be loaded for id : " + s);
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(CartLineItem.class, idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}