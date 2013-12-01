package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.subscription.SubscriptionStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/12/12
 * Time: 10:26 PM
 */
@Component
public class SubscriptionStatusTypeConverter implements TypeConverter<SubscriptionStatus> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public SubscriptionStatus convert(String id, Class<? extends SubscriptionStatus> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(SubscriptionStatus.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
