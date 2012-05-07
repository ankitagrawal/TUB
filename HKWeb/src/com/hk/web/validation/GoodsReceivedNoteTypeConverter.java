package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class GoodsReceivedNoteTypeConverter implements TypeConverter<GoodsReceivedNote> {

    
    @Autowired
    private BaseDao baseDao;

  public void setLocale(Locale locale) {
    // nothing
  }

  public GoodsReceivedNote convert(String id, Class<? extends GoodsReceivedNote> aClass, Collection<ValidationError> validationErrors) {
    //GoodsReceivedNoteDao goodsReceivedNoteDao = ServiceLocatorFactory.getService(GoodsReceivedNoteDao.class);
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(GoodsReceivedNote.class, idLong);
      //return goodsReceivedNoteDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }
}