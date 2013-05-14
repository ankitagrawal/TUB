package com.hk.web.validation;

import com.hk.domain.inventory.creditNote.CreditNoteStatus;
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
public class CreditNoteStatusTypeConverter implements TypeConverter<CreditNoteStatus> {

  @Autowired
  private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

  public CreditNoteStatus convert(String id, Class<? extends CreditNoteStatus> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      return getBaseDao().get(CreditNoteStatus.class, idLong);
    }

  }

  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

}