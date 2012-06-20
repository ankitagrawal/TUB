package com.hk.web.validation;

import com.hk.domain.core.State;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.core.StateDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 6:01:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class StateTypeConverter  implements TypeConverter<State> {

  @Autowired
  private BaseDao baseDao;


  public void setLocale(Locale locale) {
     // nothing
   }

    StateDao courierDao;

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
       //return courierDao.find(idLong);
     }

   }

  public BaseDao getBaseDao() {
       return baseDao;
   }


   public void setBaseDao(BaseDao baseDao) {
       this.baseDao = baseDao;
   }


}
