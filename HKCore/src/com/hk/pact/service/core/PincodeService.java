package com.hk.pact.service.core;

import java.util.List;

import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Zone;
import com.hk.domain.warehouse.Warehouse;

public interface PincodeService {

    public Pincode getByPincode(String pincode);

    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping);

    public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping);

    public PincodeDefaultCourier createPincodeDefaultCourier(Pincode pincode, Courier courier, Warehouse warehouse, boolean isGroundShippingAvailable, boolean isCODAvailable, Double estimatedShippingCost);

	public Zone getZoneByName(String zoneName);

    Pincode save(Pincode pincode);

    public List<Pincode> getPincodeNotInPincodeRegionZone();

    public List<Pincode> getPincodes(City city);

}
