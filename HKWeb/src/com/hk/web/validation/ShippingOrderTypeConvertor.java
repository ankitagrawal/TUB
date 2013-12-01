package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.BaseDao;


@Component
public class ShippingOrderTypeConvertor implements TypeConverter<ShippingOrder> {
    public void setLocale(Locale locale) {
        // do nothing
    }

    @Autowired
    private BaseDao baseDao;

    // ShippingOrderDao shippingOrderDao;

    public ShippingOrder convert(String s, Class<? extends ShippingOrder> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return shippingOrderDao.find(idLong);
            return getBaseDao().get(ShippingOrder.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
