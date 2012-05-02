package web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.core.Pincode;

/**
 * Generated
 */
@Component
public class PincodeTypeConverter implements TypeConverter<Pincode> {

    public void setLocale(Locale locale) {
    // nothing
  }

   //PincodeDao pincodeDao;
    @Autowired
    private BaseDao baseDao;

  public Pincode convert(String id, Class<? extends Pincode> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(Pincode.class, idLong);
   //   return pincodeDao.find(idLong);
    }

  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }


}