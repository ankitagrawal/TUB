package com.hk.pact.dao.courier;

import java.util.List;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

public interface PincodeDao extends BaseDao {

    public Pincode getByPincode(String pincode);

    public List<PincodeDefaultCourier> getByPincode(Pincode pincode);

    public PincodeDefaultCourier getByPincodeWarehouse(Pincode pincode, Warehouse warehouse);
}
