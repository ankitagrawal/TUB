package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.GrnLineItem;
import com.hk.pact.dao.BaseDao;

@Component
public class GrnLineItemTypeConverter implements TypeConverter<GrnLineItem> {
    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    // GrnLineItemDao grnLineItemDao;

    public GrnLineItem convert(String s, Class<? extends GrnLineItem> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(GrnLineItem.class, idLong);
            // return grnLineItemDao.find(idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}