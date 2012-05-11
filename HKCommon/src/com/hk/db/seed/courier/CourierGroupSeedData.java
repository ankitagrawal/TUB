package com.hk.db.seed.courier;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.pact.dao.BaseDao;

/**
 * Generated
 */
@SuppressWarnings({"InjectOfNonPublicMember"})
public class CourierGroupSeedData {

	@Autowired
	BaseDao baseDao;

	public void insert(java.lang.Long id, java.lang.String name, Courier courier) {
		CourierGroup courierGroup = new CourierGroup();
		courierGroup.setName(name);
		courierGroup.setId(id);
		courierGroup.setCourier(courier);
		getBaseDao().save(courierGroup);
	}

	public void invokeInsert() {
		List<Long> pkList = new ArrayList<Long>();

		for (EnumCourierGroup enumCourierGroup : EnumCourierGroup.values()) {

			if (pkList.contains(enumCourierGroup.getId()))
				throw new RuntimeException("Duplicate key " + enumCourierGroup.getId());
			else pkList.add(enumCourierGroup.getId());

			insert(enumCourierGroup.getId(), enumCourierGroup.getName(), courierDao.find(enumCourierGroup.getCourierId()));
		}
	}

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}
