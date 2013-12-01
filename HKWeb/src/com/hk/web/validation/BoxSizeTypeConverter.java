package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.courier.BoxSize;
import com.hk.pact.dao.BaseDao;

@Component
public class BoxSizeTypeConverter implements TypeConverter<BoxSize> {

    public void setLocale(Locale locale) {
        // nothing
    }

    // BoxSizeDao boxSizeDao;

    @Autowired
    private BaseDao baseDao;

    public BoxSize convert(String id, Class<? extends BoxSize> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(BoxSize.class, idLong);
            //return boxSizeDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}