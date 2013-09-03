package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.warehouse.Warehouse;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vidur Malhotra
 * Date: 9/2/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierPricingEngineService {

  public List<Courier> getAvailableCouriers();

  public List<RegionType> getRegionTypeList();

  public List<CourierPricingEngine> getCourierPricingInfoByCourier(Courier courier);

  public CourierPricingEngine getCourierPricingInfo(Courier courier, RegionType regionType, Warehouse warehouse);

  public CourierPricingEngine saveCourierPricingInfo(CourierPricingEngine courierPricingEngine);

  public CourierPricingEngine getCourierPricingInfoById(Long courierPricingEngineId);

  public List<RegionType> getRegionsForCourier(Courier courier);
}
