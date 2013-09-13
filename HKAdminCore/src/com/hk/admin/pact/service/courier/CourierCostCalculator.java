package com.hk.admin.pact.service.courier;

import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.warehouse.Warehouse;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/28/12
 * Time: 11:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierCostCalculator {

    public Courier getCheapestCourier(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground);

    public Long getCheapestCourierCost(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground);

    public Map.Entry<Courier, Long> getCheapestCourierEntry(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground);

    public TreeMap<Courier, Long> getCourierCostingMap(String pincode, boolean cod, Warehouse srcWarehouse,
                                                       Double amount, Double weight, boolean ground, Date shipmentDate);

    public CourierPricingEngine getCourierPricingInfo(Courier courier, Pincode pincodeObj, Warehouse srcWarehouse,
                                                      Date shipmentDate);

}
