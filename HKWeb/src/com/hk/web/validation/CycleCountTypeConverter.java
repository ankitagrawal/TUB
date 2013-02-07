package com.hk.web.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.pact.dao.BaseDao;
import com.hk.domain.cycleCount.CycleCount;

import java.util.Locale;
import java.util.Collection;

import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.TypeConverter;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 14, 2013
 * Time: 6:43:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CycleCountTypeConverter implements TypeConverter<CycleCount> {


	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {
		// nothing
	}

	public CycleCount convert(String id, Class<? extends CycleCount> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(CycleCount.class, idLong);
		}

	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}


}
