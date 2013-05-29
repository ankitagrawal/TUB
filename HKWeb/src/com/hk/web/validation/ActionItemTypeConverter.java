package com.hk.web.validation;

import com.hk.domain.queue.ActionItem;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Collection;

import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.TypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: May 20, 2013
 * Time: 5:49:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class ActionItemTypeConverter implements TypeConverter<ActionItem> {

     public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    public ActionItem convert(String id, Class<? extends ActionItem> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ActionItem.class, idLong);
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
