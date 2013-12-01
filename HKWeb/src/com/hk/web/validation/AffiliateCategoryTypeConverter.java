package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.affiliate.AffiliateCategory;
import com.hk.pact.dao.BaseDao;

@Component
public class AffiliateCategoryTypeConverter implements TypeConverter<AffiliateCategory> {

    public void setLocale(Locale locale) {
        // nothing
    }

    @Autowired
    private BaseDao baseDao;

    // AffiliateCategoryDao affiliatecategoryDao;

    public AffiliateCategory convert(String id, Class<? extends AffiliateCategory> aClass, Collection<ValidationError> validationErrors) {
        if (id == null) {
            return null;
        } else {
            return baseDao.get(AffiliateCategory.class, id);
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }


    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
      
      
}
