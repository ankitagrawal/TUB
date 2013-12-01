package com.hk.web.validation;

import com.hk.domain.courier.RegionType;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.pact.dao.BaseDao;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 17, 2012
 * Time: 12:48:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class PincodeRegionZoneTypeConverter implements TypeConverter<PincodeRegionZone> {

	@Autowired
	private BaseDao baseDao;

	public void setLocale(Locale locale) {

	}

	public PincodeRegionZone convert(String id, Class<? extends PincodeRegionZone> subClass, Collection<ValidationError> validationError) {
		Long pincodeRegionId = null;
		try {
			pincodeRegionId = Long.parseLong(id);
		} catch (NumberFormatException nf) {
		}
		if (pincodeRegionId != null) {

			return baseDao.get(PincodeRegionZone.class, pincodeRegionId);
		} else {
			return null;

		}

	}

}
