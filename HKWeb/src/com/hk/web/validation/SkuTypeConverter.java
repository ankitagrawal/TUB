package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.sku.Sku;
import com.hk.pact.dao.BaseDao;


@Component
public class SkuTypeConverter implements TypeConverter<Sku> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    // /SkuDao skuDao;

    public Sku convert(String id, Class<? extends Sku> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return skuDao.find(idLong);
            return getBaseDao().get(Sku.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
