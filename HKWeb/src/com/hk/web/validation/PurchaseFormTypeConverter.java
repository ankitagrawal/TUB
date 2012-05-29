package com.hk.web.validation;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import com.hk.domain.core.PurchaseFormType;
import com.hk.pact.dao.BaseDao;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Collection;

@Component
public class PurchaseFormTypeConverter implements TypeConverter<PurchaseFormType> {


    public void setLocale(Locale locale) {
    }

    @Autowired
    private BaseDao baseDao;

    public PurchaseFormType convert(String s, Class<? extends PurchaseFormType> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(s);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(PurchaseFormType.class, idLong);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
