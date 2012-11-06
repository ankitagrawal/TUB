package com.hk.web.validation;

import org.springframework.stereotype.Component;
import com.hk.domain.courier.CourierGroup;
import com.hk.pact.dao.BaseDao;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.Collection;

import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.TypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Sep 14, 2012
 * Time: 3:56:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CourierGroupTypeConverter implements TypeConverter<CourierGroup> {

	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {
		// nothing
	}


	public CourierGroup convert(String id, Class<? extends CourierGroup> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(CourierGroup.class, idLong);

		}

	}

	public BaseDao getBaseDao() {
		return baseDao;
	}


	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}


}
