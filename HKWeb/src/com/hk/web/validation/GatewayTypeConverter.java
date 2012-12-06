package com.hk.web.validation;

import com.hk.domain.payment.Gateway;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class GatewayTypeConverter implements TypeConverter<Gateway> {
    public void setLocale(Locale locale) {
    }


    @Autowired
    private BaseDao baseDao;

    public Gateway convert(String s, Class<? extends Gateway> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Gateway.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }


    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}

