package com.hk.web.validation;

import com.hk.domain.review.Mail;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/10/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class MailTypeConverter implements TypeConverter<Mail> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
    }


    public Mail convert(String id, Class<? extends Mail> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(Mail.class, idLong);

            // return addressDao.find(idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
