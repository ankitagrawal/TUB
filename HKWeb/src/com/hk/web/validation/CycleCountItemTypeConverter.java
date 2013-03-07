package com.hk.web.validation;


import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.pact.dao.BaseDao;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jan 15, 2013
 * Time: 5:11:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountItemTypeConverter implements TypeConverter<CycleCountItem> {

	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {
		// nothing
	}

	public CycleCountItem convert(String id, Class<? extends CycleCountItem> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(CycleCountItem.class, idLong);
		}

	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

}
