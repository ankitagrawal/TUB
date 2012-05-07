package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.TicketStatus;
import com.hk.pact.dao.BaseDao;

@Component
public class TicketStatusTypeConverter implements TypeConverter<TicketStatus> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;
  

  public TicketStatus convert(String id, Class<? extends TicketStatus> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      //return ticketStatusDao.find(idLong);
      return getBaseDao().get(TicketStatus.class, idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }


}
