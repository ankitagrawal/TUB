package com.hk.web.validation;

import com.hk.domain.catalog.product.UpdatePvPrice;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Locale;

@Component
public class UpdatePvPriceTypeConverter implements TypeConverter<UpdatePvPrice> {
	public void setLocale(Locale locale) {
		//do nothing
	}

	@Autowired
	BaseDao baseDao;

	public UpdatePvPrice convert(String s, Class<? extends UpdatePvPrice> aClass, Collection<ValidationError> validationErrors) {
		Long idLong = null;
		try {
			idLong = Long.parseLong(s);
		} catch (NumberFormatException e) {
		}
		if (idLong == null) {
			return null;
		} else {
			return baseDao.get(UpdatePvPrice.class, idLong);
		}
	}


}