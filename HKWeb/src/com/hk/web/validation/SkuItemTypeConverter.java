package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.sku.SkuItem;
import com.hk.pact.dao.BaseDao;


@Component
public class SkuItemTypeConverter implements TypeConverter<SkuItem> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public SkuItem convert(String id, Class<? extends SkuItem> aClass, Collection<ValidationError> validationErrors) {
        // SkuItemDao skuItemDao = ServiceLocatorFactory.getService(SkuItemDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return skuItemDao.find(idLong);
            return getBaseDao().get(SkuItem.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
