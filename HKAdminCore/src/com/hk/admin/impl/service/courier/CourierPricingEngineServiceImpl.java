package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.service.courier.CourierPricingEngineService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.MasterDataDao;
import com.hk.pact.dao.warehouse.WarehouseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Vidur Malhotra
 * Date: 9/2/13
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourierPricingEngineServiceImpl implements CourierPricingEngineService {

  @Autowired
  CourierPricingEngineDao courierPricingEngineDao;

  public List<RegionType> getRegionTypeList() {
    return courierPricingEngineDao.getAll(RegionType.class);
  }

  public List<CourierPricingEngine> getCourierPricingInfoByCourier(Courier courier) {
    return courierPricingEngineDao.getCourierPricingInfoByCourier(courier);
  }

  public CourierPricingEngine getCourierPricingInfo(Courier courier, RegionType regionType, Warehouse warehouse) {
    return courierPricingEngineDao.getCourierPricingInfo(courier, regionType, warehouse);
  }

  public CourierPricingEngine saveCourierPricingInfo(CourierPricingEngine courierPricingEngine) {
    return (CourierPricingEngine)courierPricingEngineDao.save(courierPricingEngine);
  }

  public CourierPricingEngine getCourierPricingInfoById(Long courierPricingEngineId) {
    return courierPricingEngineDao.get(CourierPricingEngine.class, courierPricingEngineId);
  }

  public List<RegionType> getRegionsForCourier(Courier courier) {
    return courierPricingEngineDao.getRegionsForCourier(courier);
  }
}
