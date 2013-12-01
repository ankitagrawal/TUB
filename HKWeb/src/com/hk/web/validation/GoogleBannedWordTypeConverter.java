package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.marketing.GoogleBannedWord;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class GoogleBannedWordTypeConverter implements TypeConverter<GoogleBannedWord> {

    @Autowired
    private BaseDao baseDao;

    
  public void setLocale(Locale locale) {
    // nothing
  }

  public GoogleBannedWord convert(String id, Class<? extends GoogleBannedWord> aClass, Collection<ValidationError> validationErrors) {
    //GoogleBannedWordDao googleBannedWordDao = ServiceLocatorFactory.getService(GoogleBannedWordDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(GoogleBannedWord.class, idLong);
    //  return googleBannedWordDao.find(idLong);
    }

  }
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
