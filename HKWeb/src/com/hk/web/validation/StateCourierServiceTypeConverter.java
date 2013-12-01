package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.courier.StateCourierService;
import com.hk.pact.dao.BaseDao;


@Component
public class StateCourierServiceTypeConverter implements TypeConverter<StateCourierService> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public StateCourierService convert(String id, Class<? extends StateCourierService> aClass, Collection<ValidationError> validationErrors) {
        // StateCourierServiceDao stateCourierServiceDao =
        // ServiceLocatorFactory.getService(StateCourierServiceDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return stateCourierServiceDao.find(idLong);
            return getBaseDao().get(StateCourierService.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}