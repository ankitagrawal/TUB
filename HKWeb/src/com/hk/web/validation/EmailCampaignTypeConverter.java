package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.email.EmailCampaign;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;

@Component
public class EmailCampaignTypeConverter implements TypeConverter<EmailCampaign> {

    @Autowired
    private BaseDao baseDao;

    public void setLocale(Locale locale) {
        // nothing
    }

    EmailCampaignDao emailCampaignDao;

    public EmailCampaign convert(String id, Class<? extends EmailCampaign> aClass, Collection<ValidationError> validationErrors) {
        Long idLong = null;
        try {
            idLong = Long.parseLong(id);
        } catch (NumberFormatException e) {
        }
        if (idLong == null) {
            return null;
        } else {
            return getBaseDao().get(EmailCampaign.class, idLong);
            // return emailCampaignDao.find(idLong);
        }

    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
