package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.review.UserReview;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
public class UserReviewTypeConverter implements TypeConverter<UserReview> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    BaseDao baseDao;

    public UserReview convert(String id, Class<? extends UserReview> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(UserReview.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}