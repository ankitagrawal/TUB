package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.State;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 6:01:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class StateTypeConverter  implements TypeConverter<State> {

  @Autowired
  private BaseDao baseDao;


  public void setLocale(Locale locale) {
     // nothing
   }


   public State convert(String id, Class<? extends State> aClass, Collection<ValidationError> validationErrors) {
     Long idLong = null;
     try {
       idLong = Long.parseLong(id);
     } catch (NumberFormatException e) {
     }
     if (idLong == null) {
       return null;
     } else {
         return getBaseDao().get(State.class, idLong);

     }

   }

  public BaseDao getBaseDao() {
       return baseDao;
   }


   public void setBaseDao(BaseDao baseDao) {
       this.baseDao = baseDao;
   }


}
