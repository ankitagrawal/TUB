package com.hk.web.validation;

import com.hk.domain.order.ReplacementOrderReason;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;


@Component
public class ReplacementOrderReasonTypeConverter implements TypeConverter<ReplacementOrderReason> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao            baseDao;


    public ReplacementOrderReason convert(String id, Class<? extends ReplacementOrderReason> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ReplacementOrderReason.class, idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}