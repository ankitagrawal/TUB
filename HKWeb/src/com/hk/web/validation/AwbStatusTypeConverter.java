package com.hk.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.TypeConverter;

import java.util.Collection;
import java.util.Locale;

import com.hk.domain.courier.AwbStatus;
import com.hk.pact.dao.BaseDao;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Dec 12, 2012
 * Time: 4:33:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class AwbStatusTypeConverter  implements TypeConverter<AwbStatus>{

	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {
		// nothing
	}

	public AwbStatus convert(String id, Class<? extends AwbStatus> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(id);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return getBaseDao().get(AwbStatus.class, idLong);
			// return awbDao.find(idLong);
		}

	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

}




