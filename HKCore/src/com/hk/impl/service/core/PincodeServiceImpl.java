package com.hk.impl.service.core;

import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Zone;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.PincodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PincodeServiceImpl implements PincodeService {

    @Autowired
    private PincodeDao pincodeDao;

    @Override
    public Pincode getByPincode(String pincode) {
        return getPincodeDao().getByPincode(pincode);
    }

    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
        return getPincodeDao().searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGroundshipping);
    }

    public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
        return getPincodeDao().searchPincodeDefaultCourier(pincode, warehouse, isCod, isGroundshipping);
    }

    public PincodeDefaultCourier createPincodeDefaultCourier(Pincode pincode, Courier courier, Warehouse warehouse, boolean isGroundShippingAvailable, boolean isCODAvailable, Double estimatedShippingCost) {
        PincodeDefaultCourier pincodeDefaultCourier = new PincodeDefaultCourier();
        pincodeDefaultCourier.setPincode(pincode);
        pincodeDefaultCourier.setCourier(courier);
        pincodeDefaultCourier.setWarehouse(warehouse);
        pincodeDefaultCourier.setGroundShipping(isGroundShippingAvailable);
        pincodeDefaultCourier.setCod(isCODAvailable);
        pincodeDefaultCourier.setEstimatedShippingCost(estimatedShippingCost);
        return pincodeDefaultCourier;
    }

    public Zone getZoneByName(String zoneName) {
        return getPincodeDao().getZoneByName(zoneName);
    }

    public List<Pincode> getPincodeNotInPincodeRegionZone() {
        return pincodeDao.getPincodeNotInPincodeRegionZone();
    }

    public List<Pincode> getPincodes(City city) {
        return pincodeDao.getPincodes(city);
    }


    @Override
    public Pincode save(Pincode pincode) {
        return (Pincode) pincodeDao.save(pincode);
    }

    public PincodeDao getPincodeDao() {
        return pincodeDao;
    }

    public void setPincodeDao(PincodeDao pincodeDao) {
        this.pincodeDao = pincodeDao;
    }


}
