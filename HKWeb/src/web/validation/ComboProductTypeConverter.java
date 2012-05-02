package web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.dao.BaseDao;
import com.hk.domain.catalog.product.combo.ComboProduct;

@Component
public class ComboProductTypeConverter implements TypeConverter<ComboProduct> {
  public void setLocale(Locale locale) {
  }

  
  //ComboProductDao comboProductDao;
  @Autowired
  private BaseDao baseDao;



  public ComboProduct convert(String s, Class<? extends ComboProduct> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(ComboProduct.class, idLong);
     // return comboProductDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    

}