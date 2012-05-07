package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.BaseDao;


@Component
public class SkuGroupTypeConverter implements TypeConverter<SkuGroup> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public SkuGroup convert(String id, Class<? extends SkuGroup> aClass, Collection<ValidationError> validationErrors) {
        // SkuGroupDao skuGroupDao = ServiceLocatorFactory.getService(SkuGroupDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return skuGroupDao.find(idLong);
            return getBaseDao().get(SkuGroup.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
