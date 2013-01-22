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
