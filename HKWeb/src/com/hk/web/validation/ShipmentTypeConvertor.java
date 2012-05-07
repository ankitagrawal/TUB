package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.courier.Shipment;
import com.hk.pact.dao.BaseDao;

@Component
public class ShipmentTypeConvertor implements TypeConverter<Shipment> {
    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    // ShipmentDao shipmentDao;

    public Shipment convert(String s, Class<? extends Shipment> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return shipmentDao.find(idLong);
            return getBaseDao().get(Shipment.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
