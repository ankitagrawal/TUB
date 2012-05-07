package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.accounting.DebitNote;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class DebitNoteTypeConverter implements TypeConverter<DebitNote> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    public DebitNote convert(String id, Class<? extends DebitNote> aClass, Collection<ValidationError> validationErrors) {
        // DebitNoteDao debitNoteDao = ServiceLocatorFactory.getService(DebitNoteDao.class);
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(DebitNote.class, idLong);
            // return debitNoteDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}