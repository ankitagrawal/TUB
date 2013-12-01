package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.accounting.PoLineItem;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PoLineItemTypeConverter implements TypeConverter<PoLineItem> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    public PoLineItem convert(String id, Class<? extends PoLineItem> aClass, Collection<ValidationError> validationErrors) {
        // PoLineItemDao poLineItemDao = ServiceLocatorFactory.getService(PoLineItemDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(PoLineItem.class, idLong);
            // return poLineItemDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
