//package com.hk.db.seed.courier;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.hk.constants.courier.EnumCourierGroup;
//import com.hk.domain.courier.Courier;
//import com.hk.domain.courier.CourierGroup;
//import com.hk.pact.dao.BaseDao;
//
///**
// * Generated
// */
//@Component
//public class CourierGroupSeedData {
//
//	@Autowired
//	BaseDao baseDao;
//
//	public void insert(Long id, String name, Courier courier) {
//		CourierGroup courierGroup = new CourierGroup();
//		courierGroup.setName(name);
//		courierGroup.setId(id);
//		getBaseDao().save(courierGroup);
//	}
//
//	public void invokeInsert() {
//		List<Long> pkList = new ArrayList<Long>();
//
//		for (EnumCourierGroup enumCourierGroup : EnumCourierGroup.values()) {
//
//			if (pkList.contains(enumCourierGroup.getId()))
//				throw new RuntimeException("Duplicate key " + enumCourierGroup.getId());
//			else pkList.add(enumCourierGroup.getId());
//
//			insert(enumCourierGroup.getId(), enumCourierGroup.getName(), getBaseDao().get(Courier.class, enumCourierGroup.getCourierId()));
//		}
//	}
//
//    public BaseDao getBaseDao() {
//        return baseDao;
//    }
//
//    public void setBaseDao(BaseDao baseDao) {
//        this.baseDao = baseDao;
//    }
//
//}
