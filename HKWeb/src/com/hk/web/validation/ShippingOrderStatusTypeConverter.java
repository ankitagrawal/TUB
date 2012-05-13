package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.order.ShippingOrderStatus;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;


@Component
public class ShippingOrderStatusTypeConverter implements TypeConverter<ShippingOrderStatus> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao            baseDao;


    public ShippingOrderStatus convert(String id, Class<? extends ShippingOrderStatus> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ShippingOrderStatus.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}