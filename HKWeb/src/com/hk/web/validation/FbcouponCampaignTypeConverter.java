package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.marketing.FbcouponCampaign;
import com.hk.pact.dao.BaseDao;

@Component
public class FbcouponCampaignTypeConverter implements TypeConverter<FbcouponCampaign> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // do nothing
    }

    // FbcouponCampaignDao fbcouponCampaignDao;

    public FbcouponCampaign convert(String s, Class<? extends FbcouponCampaign> aClass, Collection<ValidationError> validationErrors) {
        return getBaseDao().get(FbcouponCampaign.class, s);
        // return fbcouponCampaignDao.find(s);
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
