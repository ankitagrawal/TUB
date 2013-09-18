package com.hk.admin.pact.service.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;

import java.util.Date;
import java.util.List;

public interface CourierService {

  public List<Courier> getAllCouriers();

  public List<Courier> getAllActiveCourier();

  public Courier getCourierById(Long courierId);

  public Courier getCourierByName(String name);

  public Courier save(Courier courier);

  public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean active, Long operationBitset);

  public Page getCouriers(String courierName, Boolean active, String courierGroup, int page, int perPage, Long operationsBitset);

  public void saveOrUpdate(Courier courier);

  public List<RegionType> getRegionTypeList();

  public List<CourierPricingEngine> getCourierPricingInfoByCourier(Courier courier);

  public void saveUpdateCourierPricingInfo(List<CourierPricingEngine> courierPricingEngines);

  public List<RegionType> getRegionsForCourier(Courier courier);

  public List<HKReachPricingEngine> searchHKReachPricing(Warehouse warehouse, Hub hub);

  public HKReachPricingEngine getHkReachPricingEngine(Warehouse warehouse, Hub hub, Date shipDate);

  public HKReachPricingEngine saveHKReachPricingEngine(HKReachPricingEngine hkReachPricingEngine);

}
