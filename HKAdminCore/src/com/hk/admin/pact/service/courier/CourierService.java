package com.hk.admin.pact.service.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

public interface CourierService {

	public List<Courier> getAllCouriers();

	public Courier getCourierById(Long courierId);

	public Courier getCourierByName(String name);

	public boolean isCodAllowed(String pin);

	public List<Courier> getAvailableCouriers(Order order);

	public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD, boolean isGroundShipping);

	public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);

	public boolean isGroundShippingAllowed(String pin);

	public boolean isCodAllowedOnGroundShipping(String pin);

	public List<Courier> getAvailableCouriers(String pinCode, boolean isCOD, boolean isGroundShipping, boolean isCodAvailableOnGroundShipping ,Boolean disabled);

	public List<CourierServiceInfo> getCourierServiceInfoList(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

	public CourierServiceInfo searchCourierServiceInfo(Long courierId, String pincode, boolean forCOD, boolean forGroundShipping, boolean forCodAvailableOnGroundShipping);

	public Courier save(Courier courier);

	public List<Courier> getCouriers(List<Long> courierIds ,List<String> courierNames , Boolean disabled);

}
