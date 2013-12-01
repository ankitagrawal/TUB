package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.coupon.FbcouponUser;
import com.hk.pact.dao.BaseDao;

@Component
public class FbcouponUserTypeConverter implements TypeConverter<FbcouponUser> {

    public void setLocale(Locale locale) {
        // do nothing
    }

    // FbcouponUserDao fbcouponUserDao;

    @Autowired
    private BaseDao baseDao;

    public FbcouponUser convert(String s, Class<? extends FbcouponUser> aClass, Collection<ValidationError> validationErrors) {
        Long fbcouponUserId = null;
        try {
            fbcouponUserId = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (fbcouponUserId == null) {
            return null;
        }
        return getBaseDao().get(FbcouponUser.class, fbcouponUserId);
        // return fbcouponuserDao.getUserById(fbcouponUserId);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
