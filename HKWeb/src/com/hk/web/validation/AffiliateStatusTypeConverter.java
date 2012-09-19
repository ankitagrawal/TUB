package com.hk.web.validation;

import com.hk.domain.affiliate.AffiliateStatus;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class AffiliateStatusTypeConverter implements TypeConverter<AffiliateStatus> {
	public void setLocale(Locale locale) {
	}


	//AffiliateDao affiliateDao;
	@Autowired
	private BaseDao baseDao;


	public AffiliateStatus convert(String id, Class<? extends AffiliateStatus> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(AffiliateStatus.class, idLong);
			// return affiliateDao.find(idLong);
		}
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}


	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}


}
