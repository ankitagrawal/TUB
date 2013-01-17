package com.hk.admin.pact.service.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.akube.framework.dao.Page;

import java.util.List;

public interface CourierService {

	public List<Courier> getAllCouriers();
	public Courier getCourierById(Long courierId);
	public Courier getCourierByName(String name);
    public Courier save(Courier courier);
    public List<Courier> getCouriers(List<Long> courierIds ,List<String> courierNames , Boolean disabled);


    public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD, boolean isGroundShipping);
    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, boolean isGroundShipping, Warehouse warehouse);
    public List<Courier> listOfVendorCouriers();

	public Page getCouriers(String courierName,Boolean disabled, String courierGroup,int page, int perPage);

	public void saveOrUpdate(Courier courier);
}
