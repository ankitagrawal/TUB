package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.hkDelivery.Hub;
import com.hk.pact.dao.BaseDao;

@Component
public class HubTypeConverter implements TypeConverter<Hub> {

    @Override
    public void setLocale(Locale locale) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Autowired
    private BaseDao baseDao;

    public Hub convert(String s, Class<? extends Hub> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            //return taxDao.find(idLong);
            return getBaseDao().get(Hub.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
