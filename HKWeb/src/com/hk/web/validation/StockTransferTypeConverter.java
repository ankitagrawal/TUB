package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.inventory.StockTransfer;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA. User: Developer Date: Apr 27, 2012 Time: 12:04:21 PM To change this template use File |
 * Settings | File Templates.
 */
public class StockTransferTypeConverter implements TypeConverter<StockTransfer> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public StockTransfer convert(String id, Class<? extends StockTransfer> aClass, Collection<ValidationError> validationErrors) {
        // StockTransferDao stockTransferDao = ServiceLocatorFactory.getService(StockTransferDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(StockTransfer.class, idLong);
        }

    }
    
    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
    
}
