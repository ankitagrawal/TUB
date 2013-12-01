package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.coupon.Coupon;
import com.hk.pact.dao.BaseDao;

@Component
public class CouponTypeConverter implements TypeConverter<Coupon> {

  public void setLocale(Locale locale) {
    // nothing
  }

   //CouponDao couponDao;
  @Autowired
  private BaseDao baseDao;


  public Coupon convert(String id, Class<? extends Coupon> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Coupon.class, idLong);
      //return couponDao.find(idLong);
    }

  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    
}
