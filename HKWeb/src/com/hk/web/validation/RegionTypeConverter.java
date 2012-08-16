package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.courier.RegionType;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User: Seema
 * Date: Jun 8, 2012
 * Time: 3:13:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RegionTypeConverter implements TypeConverter<RegionType> {
  @Autowired
    private BaseDao baseDao;

   public void setLocale(Locale locale) {

  }

  public RegionType convert(String id,Class<? extends RegionType> subClass , Collection<ValidationError> validationError){
 Long regionId=null;
    try{
   regionId= Long.parseLong(id);
    }catch(NumberFormatException nf){
    }
    if(regionId != null){
      
    return baseDao.get(RegionType.class,regionId) ;
    }
      else{
      return  null;

    }

  }




  public BaseDao getBaseDao() {
    return baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

}
