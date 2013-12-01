package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.inventory.StockTransferLineItem;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA. User: Developer Date: May 1, 2012 Time: 11:00:52 AM To change this template use File |
 * Settings | File Templates.
 */
public class StockTransferLineItemTypeConverter implements TypeConverter<StockTransferLineItem> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public StockTransferLineItem convert(String id, Class<? extends StockTransferLineItem> aClass, Collection<ValidationError> validationErrors) {
        // StockTransferLineItemDao stockTransferLineItemDao =
        // ServiceLocatorFactory.getService(StockTransferLineItemDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(StockTransferLineItem.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
