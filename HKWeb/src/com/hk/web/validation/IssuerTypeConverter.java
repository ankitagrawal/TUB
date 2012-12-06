package com.hk.web.validation;

import com.hk.domain.payment.Issuer;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class IssuerTypeConverter implements TypeConverter<Issuer> {
    public void setLocale(Locale locale) {
    }


    @Autowired
    private BaseDao baseDao;

    public Issuer convert(String s, Class<? extends Issuer> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Issuer.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }


    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}

