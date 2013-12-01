package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.offer.rewardPoint.RewardPointMode;
import com.hk.pact.dao.BaseDao;

@Component
public class RewardPointModeTypeConverter implements TypeConverter<RewardPointMode> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    // RewardPointModeDao rewardPointModeDao;

    public RewardPointMode convert(String id, Class<? extends RewardPointMode> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(RewardPointMode.class, idLong);
            // return rewardPointModeDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}