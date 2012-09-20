package com.hk.admin.pact.service.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.Order;
import com.hk.domain.warehouse.Warehouse;

public interface CourierService {

    public List<Courier> getAllCouriers();

    public Courier getCourierById(Long courierId);
    
    public Courier getCourierByName(String name);
    
    public CourierServiceInfo getCourierServiceByPincodeAndCourier(Long courierId, String pincode, Boolean isCod);

    /**
     * cod not available if either courier service not available there or order is exclusively service order -->
     * changing to if any service is there
     */
    public boolean isCodAllowed(String pin);

    public List<Courier> getAvailableCouriers(Order order);

    public List<Courier> getAvailableCouriers(String pinCode, boolean isCOD);

    public Courier getDefaultCourierByPincodeForLoggedInWarehouse(Pincode pincode, boolean isCOD);

    public Courier getDefaultCourier(Pincode pincode, boolean isCOD, Warehouse warehouse);

	public Courier save(Courier courier);


}
