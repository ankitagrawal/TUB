package com.hk.web.validation;

/*
 * User: Pratham
 * Date: 26/03/13  Time: 18:32
*/

import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;


@Component
public class ShippingOrderLifecycleTypeConverter implements TypeConverter<ShippingOrderLifecycle> {
    public void setLocale(Locale locale) {
        //do nothing
    }

    @Autowired
    private BaseDao baseDao;

    public ShippingOrderLifecycle convert(String s, Class<? extends ShippingOrderLifecycle> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ShippingOrderLifecycle.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }


    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }



}
