package com.hk.pact.service.core;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;

public interface PincodeService {

	//todo ankit, if not a PDCService, atlease create a separate a different PDCdao for this  --- didnt get u

    
    public Pincode getByPincode(String pincode) ;

    public List<PincodeDefaultCourier> getByPincode(Pincode pincode) ;

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse) ;

     public List<PincodeDefaultCourier>  getByPincode(Pincode pincode,  boolean isCod, boolean isGroundshipping) ;

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse,  boolean isCod, boolean isGroundshipping) ;

    public PincodeDefaultCourier searchPincodeDefaultCourier(Pincode pincode, Warehouse warehouse, Boolean isCod, Boolean isGroundshipping) ;
}
