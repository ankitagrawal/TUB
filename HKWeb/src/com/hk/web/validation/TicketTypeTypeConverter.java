package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import com.hk.dao.BaseDao;
import com.hk.domain.TicketType;


@Component
public class TicketTypeTypeConverter implements TypeConverter<TicketType> {

  public void setLocale(Locale locale) {
    // nothing
  }

  @Autowired
  private BaseDao baseDao;
  
   //TicketTypeDao ticketTypeDao;

  public TicketType convert(String id, Class<? extends TicketType> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
      //return ticketTypeDao.find(idLong);
      return getBaseDao().get(TicketType.class, idLong);
    }

  }

public BaseDao getBaseDao() {
    return baseDao;
}

public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
}

  

}
