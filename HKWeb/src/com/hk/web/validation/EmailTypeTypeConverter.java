package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.EmailType;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class EmailTypeTypeConverter implements TypeConverter<EmailType> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    // EmailTypeDao emailTypeDao;

    public EmailType convert(String id, Class<? extends EmailType> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(EmailType.class, idLong);
            // return emailTypeDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
