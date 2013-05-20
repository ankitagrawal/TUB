package com.hk.web.validation;

import com.hk.domain.inventory.creditNote.CreditNote;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/**
 * Generated
 */
@Component
public class CreditNoteTypeConverter implements TypeConverter<CreditNote> {

  @Autowired
  private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

  public CreditNote convert(String id, Class<? extends CreditNote> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return getBaseDao().get(CreditNote.class, idLong);
    }

  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

}