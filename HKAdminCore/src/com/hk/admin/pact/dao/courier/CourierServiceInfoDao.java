package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface CourierServiceInfoDao extends BaseDao {
	//todo ankit dont be specific in method names, better rename to searchCourierServiceInfo, searchDefaultCourier etc

	public Courier getDefaultCourierForPincode(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);

    public List<Courier> getCouriersForPincode(String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

    public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

    public CourierServiceInfo getCourierServiceInfoForPincode(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

    public boolean isCourierServiceInfoAvailable(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

}
