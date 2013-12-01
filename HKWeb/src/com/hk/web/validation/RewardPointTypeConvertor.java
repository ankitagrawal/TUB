package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.pact.dao.BaseDao;

public class RewardPointTypeConvertor implements TypeConverter<RewardPoint> {

    
    private static Logger logger = LoggerFactory.getLogger(RewardPointTypeConvertor.class);

    public void setLocale(Locale locale) {
    }

    
    @Autowired
    private BaseDao baseDao;


    public RewardPoint convert(String s, Class<? extends RewardPoint> aClass, Collection<ValidationError> validationErrors) {
      Long idLong = null;
      try {
        idLong = Long.parseLong(s);
      } catch (NumberFormatException e) {
        logger.error("cart line item could not be loaded for id : " + s);
      }
      if (idLong == null) {
        return null;
      } else {
          return getBaseDao().get(RewardPoint.class, idLong);
      }
    }
    
    public BaseDao getBaseDao() {
        return baseDao;
    }


    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
