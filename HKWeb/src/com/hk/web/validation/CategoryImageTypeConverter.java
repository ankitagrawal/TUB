package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.catalog.category.CategoryImage;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: meenal
 * Date: Jan 13, 2012
 * Time: 3:08:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CategoryImageTypeConverter implements TypeConverter<CategoryImage> {
    public void setLocale(Locale locale) {
  }

  
  //CategoryImageDao categoryImageDao;
    @Autowired
    private BaseDao baseDao;


  public CategoryImage convert(String s, Class<? extends CategoryImage> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(s);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(CategoryImage.class, idLong);
      //return categoryImageDao.find(idLong);
    }
  }
  
  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
    
    
}
