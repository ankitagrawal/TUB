package com.hk.web.validation;

import java.util.Collection;
import java.util.Locale;

import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.domain.content.SeoData;
import com.hk.pact.dao.BaseDao;
import com.hk.util.SeoManager;

@Component
public class SeoDataTypeConverter implements TypeConverter<SeoData> {
    @Autowired
    private SeoManager seoManager;

    public void setLocale(Locale locale) {

    }

    @Autowired
    private BaseDao baseDao;

    // SeoDataDao seoDataDao;

    public SeoData convert(String s, Class<? extends SeoData> aClass, Collection<ValidationError> validationErrors) {
        if (StringUtils.isBlank(s)) {
            return null;
        } else if (getBaseDao().get(SeoData.class,s) != null) {
            return getBaseDao().get(SeoData.class, s);
           // return seoDataDao.find(s);
        } else {
	        String[] sArr = StringUtils.split(s, "||");
	        if (StringUtils.isNotBlank(s) && getBaseDao().get(SeoData.class, sArr[0]) != null) {
		        return getBaseDao().get(SeoData.class, sArr[0]);
		        // return seoDataDao.find(s);
	        } else {
		        return seoManager.generateSeo(s);
	        }
        }
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }
}
