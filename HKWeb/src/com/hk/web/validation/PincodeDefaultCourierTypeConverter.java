package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.pact.dao.BaseDao;



@Component
public class PincodeDefaultCourierTypeConverter implements TypeConverter<PincodeDefaultCourier> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    // PincodeDefaultCourierDao pincodeDefaultCourierDao;

    public PincodeDefaultCourier convert(String id, Class<? extends PincodeDefaultCourier> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(PincodeDefaultCourier.class, idLong);
            // /return pincodeDefaultCourierDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}