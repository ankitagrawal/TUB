package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.impl.dao.ReconciliationStatusDaoImpl;
import com.hk.pact.dao.BaseDao;

@Component
public class ReconciliationStatusTypeConverter implements TypeConverter<ReconciliationStatus> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    ReconciliationStatusDaoImpl reconciliationStatusDao;

    public ReconciliationStatus convert(String id, Class<? extends ReconciliationStatus> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ReconciliationStatus.class, idLong);
            // return reconciliationStatusDao.find(idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
