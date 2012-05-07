package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.po.PurchaseInvoiceStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class PurchaseInvoiceStatusTypeConverter implements TypeConverter<PurchaseInvoiceStatus> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public PurchaseInvoiceStatus convert(String id, Class<? extends PurchaseInvoiceStatus> aClass, Collection<ValidationError> validationErrors) {
        // PurchaseInvoiceStatusDao purchaseInvoiceStatusDao =
        // ServiceLocatorFactory.getService(PurchaseInvoiceStatusDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(PurchaseInvoiceStatus.class, idLong);
            // //return purchaseInvoiceStatusDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
