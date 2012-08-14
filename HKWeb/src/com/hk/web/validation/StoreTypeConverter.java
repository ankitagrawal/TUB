
package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.store.Store;
import com.hk.pact.dao.BaseDao;
/**
 * Created with IntelliJ IDEA.
 * User: Pradeep
 * Date: 6/22/12
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class StoreTypeConverter implements TypeConverter<Store> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public Store convert(String id, Class<? extends Store> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return surchargeDao.find(idLong);
            return getBaseDao().get(Store.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}

