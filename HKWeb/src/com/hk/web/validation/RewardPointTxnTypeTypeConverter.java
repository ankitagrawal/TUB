package com.hk.web.validation;


import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.rewardPoint.RewardPointTxnType;
import com.hk.pact.dao.BaseDao;


@Component
public class RewardPointTxnTypeTypeConverter implements TypeConverter<RewardPointTxnType> {

    
    @Autowired
    private BaseDao baseDao;
    
  public void setLocale(Locale locale) {
    // nothing
  }

   //RewardPointTxnTypeDao rewardPointTxnTypeDao;

  public RewardPointTxnType convert(String id, Class<? extends RewardPointTxnType> aClass, Collection<ValidationError> validationErrors) {
    Long idLong = null;
    try {
      idLong = Long.parseLong(id);
    } catch (NumberFormatException e) {
    }
    if (idLong == null) {
      return null;
    } else {
        return getBaseDao().get(RewardPointTxnType.class, idLong);
     // return rewardPointTxnTypeDao.find(idLong);
    }

  }

  public BaseDao getBaseDao() {
      return baseDao;
  }


  public void setBaseDao(BaseDao baseDao) {
      this.baseDao = baseDao;
  }

}
