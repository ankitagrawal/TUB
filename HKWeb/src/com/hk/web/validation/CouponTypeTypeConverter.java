package com.hk.web.validation;

import com.hk.domain.coupon.CouponType;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class CouponTypeTypeConverter implements TypeConverter<CouponType> {

	public void setLocale(Locale locale) {
		// nothing
	}

	//CouponDao couponDao;
	@Autowired
	private BaseDao baseDao;


	public CouponType convert(String id, Class<? extends CouponType> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(CouponType.class, idLong);
			//return couponDao.find(idLong);
		}

	}

	public BaseDao getBaseDao() {
		return baseDao;
	}


	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}


}
