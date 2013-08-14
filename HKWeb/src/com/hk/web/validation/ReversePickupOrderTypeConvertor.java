package com.hk.web.validation;

import com.hk.domain.reversePickupOrder.ReversePickupOrder;
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
 * Time: 9:19 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class ReversePickupOrderTypeConvertor implements TypeConverter<ReversePickupOrder> {

    public void setLocale(Locale locale) {
        // do nothing
    }

    @Autowired
    private BaseDao baseDao;

    public ReversePickupOrder convert(String s, Class<? extends ReversePickupOrder> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ReversePickupOrder.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }


}
