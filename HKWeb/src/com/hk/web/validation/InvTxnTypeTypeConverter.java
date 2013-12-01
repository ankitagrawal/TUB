package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.core.InvTxnType;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class InvTxnTypeTypeConverter implements TypeConverter<InvTxnType> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public InvTxnType convert(String id, Class<? extends InvTxnType> aClass, Collection<ValidationError> validationErrors) {
        // InvTxnTypeDao invTxnTypeDao = ServiceLocatorFactory.getService(InvTxnTypeDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(InvTxnType.class, idLong);
            // return invTxnTypeDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
