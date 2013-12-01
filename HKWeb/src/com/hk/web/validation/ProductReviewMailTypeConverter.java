package com.hk.web.validation;


import com.hk.domain.review.ProductReviewMail;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class ProductReviewMailTypeConverter implements TypeConverter<ProductReviewMail> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
    }


    public ProductReviewMail convert(String id, Class<? extends ProductReviewMail> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(ProductReviewMail.class, idLong);

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
