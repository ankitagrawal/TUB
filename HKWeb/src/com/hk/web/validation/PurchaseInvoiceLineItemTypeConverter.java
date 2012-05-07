package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.po.PurchaseInvoiceLineItem;
import com.hk.pact.dao.BaseDao;

@Component
public class PurchaseInvoiceLineItemTypeConverter implements TypeConverter<PurchaseInvoiceLineItem> {
    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    // PurchaseInvoiceLineItemDao purchaseInvoiceLineItemDao;

    public PurchaseInvoiceLineItem convert(String s, Class<? extends PurchaseInvoiceLineItem> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(PurchaseInvoiceLineItem.class, idLong);
            // return purchaseInvoiceLineItemDao.find(idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}