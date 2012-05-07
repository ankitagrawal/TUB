package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.user.Address;
import com.hk.pact.dao.BaseDao;

/**
 * User: rahul Time: 4 Dec, 2009 4:53:15 PM
 */
@Component
public class AddressTypeConverter implements TypeConverter<Address> {
    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    public Address convert(String id, Class<? extends Address> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Address.class, idLong);
            // return addressDao.find(idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
