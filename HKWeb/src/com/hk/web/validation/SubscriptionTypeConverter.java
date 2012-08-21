package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.subscription.Subscription;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/6/12
 * Time: 11:28 AM
 */
@Component
public class SubscriptionTypeConverter implements TypeConverter<Subscription> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;

  public Subscription convert(String id, Class<? extends Subscription> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return getBaseDao().get(Subscription.class, idLong);
    }
  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

}

