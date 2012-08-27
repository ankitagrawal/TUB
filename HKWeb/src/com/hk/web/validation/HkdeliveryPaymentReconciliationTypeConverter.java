package com.hk.web.validation;

import com.hk.domain.hkDelivery.HkdeliveryPaymentReconciliation;
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
public class HkdeliveryPaymentReconciliationTypeConverter implements TypeConverter<HkdeliveryPaymentReconciliation> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    public HkdeliveryPaymentReconciliation convert(String id, Class<? extends HkdeliveryPaymentReconciliation> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(HkdeliveryPaymentReconciliation.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}