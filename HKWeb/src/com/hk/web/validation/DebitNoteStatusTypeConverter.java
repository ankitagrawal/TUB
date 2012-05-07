package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.accounting.DebitNoteStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class DebitNoteStatusTypeConverter implements TypeConverter<DebitNoteStatus> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    public DebitNoteStatus convert(String id, Class<? extends DebitNoteStatus> aClass, Collection<ValidationError> validationErrors) {
        // DebitNoteStatusDao debitNoteStatusDao = ServiceLocatorFactory.getService(DebitNoteStatusDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(DebitNoteStatus.class, idLong);
            // return debitNoteStatusDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}