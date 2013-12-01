package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.pact.dao.BaseDao;

/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 7/17/12
 * Time: 4:06 PM
 */
@Component
public class SubscriptionProductTypeConverter implements TypeConverter<SubscriptionProduct> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public SubscriptionProduct convert(String id, Class<? extends SubscriptionProduct> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(SubscriptionProduct.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}

