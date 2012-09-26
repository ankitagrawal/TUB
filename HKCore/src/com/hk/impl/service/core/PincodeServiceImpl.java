package com.hk.impl.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.core.PincodeService;

@Service
public class PincodeServiceImpl implements PincodeService {
    
    @Autowired
    private PincodeDao pincodeDao;

    @Override
    public Pincode getByPincode(String pincode) {
       return getPincodeDao().getByPincode(pincode);
    }

    public List<PincodeDefaultCourier>  getByPincode(Pincode pincode,  boolean isCod, boolean isGroundshipping) {
        return   getPincodeDao().getByPincode(pincode , isCod, isGroundshipping);
    }          

     public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
          return getPincodeDao().searchPincodeDefaultCourier(pincode, warehouse, isCod, isGroundshipping);
     }


    public PincodeDao getPincodeDao() {
        return pincodeDao;
    }

    public void setPincodeDao(PincodeDao pincodeDao) {
        this.pincodeDao = pincodeDao;
    }
  
    
}
