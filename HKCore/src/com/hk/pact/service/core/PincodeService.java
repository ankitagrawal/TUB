package com.hk.pact.service.core;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;

public interface PincodeService {

    
    public Pincode getByPincode(String pincode) ;

    public List<PincodeDefaultCourier> getByPincode(Pincode pincode) ;

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse) ;

     public List<PincodeDefaultCourier>  getByPincode(Pincode pincode,  boolean forCod, boolean forGroundshipping) ;

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse,  boolean forCod, boolean forGroundshipping) ;
}
