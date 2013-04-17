package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.pact.dao.BaseDao;


@Component
public class ReverseOrderTypeConvertor implements TypeConverter<ReverseOrder> {
    public void setLocale(Locale locale) {
        // do nothing
    }

    @Autowired
    private BaseDao baseDao;

    // ShippingOrderDao shippingOrderDao;

    public ReverseOrder convert(String s, Class<? extends ReverseOrder> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return shippingOrderDao.find(idLong);
            return getBaseDao().get(ReverseOrder.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
