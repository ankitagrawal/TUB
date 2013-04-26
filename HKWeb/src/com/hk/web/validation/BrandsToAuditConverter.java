//package com.hk.web.validation;
//
//import java.util.Collection;
//import java.util.Locale;
//
//import net.sourceforge.stripes.validation.TypeConverter;
//import net.sourceforge.stripes.validation.ValidationError;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.hk.domain.inventory.BrandsToAudit;
//import com.hk.pact.dao.BaseDao;
//
///**
// * Generated
// */
//@Component
//public class BrandsToAuditConverter implements TypeConverter<BrandsToAudit> {
//
//	@Autowired
//	private BaseDao baseDao;
//
//	public void setLocale(Locale locale) {
//		// nothing
//	}
//
//	public BrandsToAudit convert(String id, Class<? extends BrandsToAudit> aClass, Collection<ValidationError> validationErrors) {
//		Long idLong = null;
//		try {
//			idLong = Long.parseLong(id);
//		} catch (NumberFormatException e) {
//		}
//		if (idLong == null) {
//			return null;
//		} else {
//			return getBaseDao().get(BrandsToAudit.class, idLong);
//		}
//
//	}
//
//	public BaseDao getBaseDao() {
//		return baseDao;
//	}
//
//	public void setBaseDao(BaseDao baseDao) {
//		this.baseDao = baseDao;
//	}
//
//}