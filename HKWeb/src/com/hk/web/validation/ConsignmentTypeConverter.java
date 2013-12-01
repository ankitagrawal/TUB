package com.hk.web.validation;

import com.hk.domain.hkDelivery.Consignment;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/**
 * Generated
 */
@Component
public class ConsignmentTypeConverter implements TypeConverter<Consignment> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    public Consignment convert(String id, Class<? extends Consignment> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Consignment.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}