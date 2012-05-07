package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.courier.Awb;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@Component
public class AwbTypeConverter implements TypeConverter<Awb> {

    public void setLocale(Locale locale) {
        // nothing
    }

    // AwbDao awbDao;
    @Autowired
    private BaseDao baseDao;

    public Awb convert(String id, Class<? extends Awb> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Awb.class, idLong);
            // return awbDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}