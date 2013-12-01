package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.City;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 20, 2012
 * Time: 7:16:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CityTypeConverter  implements TypeConverter<City> {

  @Autowired
  private BaseDao baseDao;


  public void setLocale(Locale locale) {
     // nothing
   }

   public City convert(String id, Class<? extends City> aClass, Collection<ValidationError> validationErrors) {
     Long idLong = null;
     try {
       idLong = Long.parseLong(id);
     } catch (NumberFormatException e) {
     }
     if (idLong == null) {
       return null;
     } else {
         return getBaseDao().get(City.class, idLong);
      
     }

   }

  public BaseDao getBaseDao() {
       return baseDao;
   }


   public void setBaseDao(BaseDao baseDao) {
       this.baseDao = baseDao;
   }


}
