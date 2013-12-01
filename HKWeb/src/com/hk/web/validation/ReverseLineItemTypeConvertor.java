package com.hk.web.validation;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Collection;

import com.hk.pact.dao.BaseDao;
import com.hk.domain.reverseOrder.ReverseLineItem;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.TypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Mar 15, 2013
 * Time: 5:11:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReverseLineItemTypeConvertor implements TypeConverter<ReverseLineItem> {
	 public void setLocale(Locale locale) {
        // do nothing
    }

    @Autowired
    private BaseDao baseDao;

    // ShippingOrderDao shippingOrderDao;

    public ReverseLineItem convert(String s, Class<? extends ReverseLineItem> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            // return shippingOrderDao.find(idLong);
            return getBaseDao().get(ReverseLineItem.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
