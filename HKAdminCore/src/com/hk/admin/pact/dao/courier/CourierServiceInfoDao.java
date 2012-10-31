package com.hk.admin.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface CourierServiceInfoDao extends BaseDao {  	

	public Courier searchDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);

    public List<Courier> searchCouriers(String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping ,Boolean disabled);

    public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping ,Boolean active);

    public CourierServiceInfo searchCourierServiceInfo(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

    public boolean isCourierServiceInfoAvailable(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

}
