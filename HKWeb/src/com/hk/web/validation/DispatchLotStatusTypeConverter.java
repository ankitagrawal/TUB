package com.hk.web.validation;

import com.hk.domain.courier.DispatchLotStatus;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/10/12
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class DispatchLotStatusTypeConverter implements TypeConverter<DispatchLotStatus> {

	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {
		// nothing
	}

	public DispatchLotStatus convert(String id, Class<? extends DispatchLotStatus> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}

		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(DispatchLotStatus.class, idLong);
		}
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
