package com.hk.web.validation;

import com.hk.domain.analytics.Reason;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/*
 * User: Pratham
 * Date: 26/03/13  Time: 16:13
*/
@Component
public class ReasonTypeConverter implements TypeConverter<Reason> {

    public void setLocale(Locale locale) {
        // nothing
    }

    // AwbDao awbDao;
    @Autowired
    private BaseDao baseDao;

    public Reason convert(String id, Class<? extends Reason> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Reason.class, idLong);
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
