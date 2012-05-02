package com.hk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.dao.courier.PincodeDao;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.service.PincodeService;

@Service
public class PincodeServiceImpl implements PincodeService {
    
    @Autowired
    private PincodeDao pincodeDao;

    @Override
    public Pincode getByPincode(String pincode) {
       return getPincodeDao().getByPincode(pincode);
    }

    @Override
    public List<PincodeDefaultCourier> getByPincode(Pincode pincode) {
        return getPincodeDao().getByPincode(pincode);
    }

    @Override
    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse) {
        return getPincodeDao().getByPincodeWarehouse(pincode, warehouse);
    }

    public PincodeDao getPincodeDao() {
        return pincodeDao;
    }

    public void setPincodeDao(PincodeDao pincodeDao) {
        this.pincodeDao = pincodeDao;
    }

    
    
}
