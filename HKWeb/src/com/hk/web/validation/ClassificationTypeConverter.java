package com.hk.web.validation;

import com.hk.domain.queue.Classification;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/24/13
 * Time: 1:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClassificationTypeConverter implements TypeConverter<Classification> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public Classification convert(String id, Class<? extends Classification> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Classification.class, idLong);
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
