package com.hk.web.validation;

import com.hk.domain.reversePickupOrder.RpLineItem;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Seema
 * Date: 7/22/13
 * Time: 9:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class RpLineItemTypeConvertor implements TypeConverter<RpLineItem> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    public RpLineItem convert(String id, Class<? extends RpLineItem> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(RpLineItem.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }


}
