package com.hk.impl.service.core;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Courier;
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


    public List<PincodeDefaultCourier> searchPincodeDefaultCourierList(Pincode pincode,Warehouse warehouse, Boolean isCod, Boolean isGroundshipping){
        return getPincodeDao().searchPincodeDefaultCourierList(pincode, warehouse, isCod, isGroundshipping);
    }

     public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) {
          return getPincodeDao().searchPincodeDefaultCourier(pincode, warehouse, isCod, isGroundshipping);
     }


      public PincodeDefaultCourier createPincodeDefaultCourier (Pincode pincode, Courier courier, Warehouse warehouse,boolean isGroundShippingAvailable, boolean isCODAvailable, Double estimatedShippingCost  ){
          PincodeDefaultCourier pincodeDefaultCourier = new PincodeDefaultCourier();
          pincodeDefaultCourier.setPincode(pincode);
          pincodeDefaultCourier.setCourier(courier);
          pincodeDefaultCourier.setWarehouse(warehouse);
          pincodeDefaultCourier.setGroundShipping(isGroundShippingAvailable);
          pincodeDefaultCourier.setCod(isCODAvailable);
          pincodeDefaultCourier.setEstimatedShippingCost(estimatedShippingCost);
          return pincodeDefaultCourier;
      }

    public PincodeDao getPincodeDao() {
        return pincodeDao;
    }

    public void setPincodeDao(PincodeDao pincodeDao) {
        this.pincodeDao = pincodeDao;
    }
  
    
}
