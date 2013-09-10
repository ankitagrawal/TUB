package com.hk.admin.impl.service.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.dao.courier.PincodeCourierMappingDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CourierServiceImpl implements CourierService {

  @Autowired
  private CourierDao courierDao;
  @Autowired
  private UserService userService;
  @Autowired
  PincodeCourierMappingDao pincodeCourierMappingDao;

  @Autowired
  CourierPricingEngineDao courierPricingEngineDao;

  @Override
  public Courier getCourierById(Long courierId) {
    List<Courier> courierList = getCourierDao().getCourierByIds(Arrays.asList(courierId));
    return courierList != null && courierList.size() > 0 ? courierList.get(0) : null;
  }

  @Override
  public Courier getCourierByName(String name) {
    if (name != null) {
      List<String> nameList = new ArrayList<String>();
      nameList.add(name);
      List<Courier> courierList = getCourierDao().getCouriers(null, nameList, null, null);
      return courierList.size() > 0 ? courierList.get(0) : null;
    }
    return null;
  }

  public List<Courier> getCouriers(List<Long> courierIds, List<String> courierNames, Boolean active, Long operationBitset) {
    return courierDao.getCouriers(courierIds, courierNames, active, operationBitset);
  }

  public Page getCouriers(String courierName, Boolean active, String courierGroup, int page, int perPage, Long operationsBitset) {
    return courierDao.getCouriers(courierName, active, courierGroup, page, perPage, operationsBitset);
  }

  public Courier save(Courier courier) {
    return (Courier) getCourierDao().save(courier);

  }

  public void saveOrUpdate(Courier courier) {
    courierDao.saveOrUpdate(courier);
  }


  public List<Courier> getAllCouriers() {
    return getCourierDao().getAll(Courier.class);
  }

  @SuppressWarnings("unchecked")
  public List<Courier> getAllActiveCourier(){
    return (List<Courier>)courierDao.findByNamedQuery("allActiveCouriers");
  }

  public List<RegionType> getRegionTypeList() {
    return courierPricingEngineDao.getAll(RegionType.class);
  }

  public List<CourierPricingEngine> getCourierPricingInfoByCourier(Courier courier) {
    return courierPricingEngineDao.getCourierPricingInfoByCourier(courier);
  }

  public void saveUpdateCourierPricingInfo(List<CourierPricingEngine> courierPricingEngines) {
     courierPricingEngineDao.saveOrUpdate(courierPricingEngines);
  }


  public List<RegionType> getRegionsForCourier(Courier courier) {
    return courierPricingEngineDao.getRegionsForCourier(courier);
  }

  public List<HKReachPricingEngine> searchHKReachPricing(Warehouse warehouse, Hub hub) {
    return courierPricingEngineDao.getHkReachPricingEngineList(warehouse, hub);
  }

  public HKReachPricingEngine getHkReachPricingEngine(Warehouse warehouse, Hub hub) {
    if (warehouse != null && hub != null) {
      return  courierPricingEngineDao.getHkReachPricingEngine(warehouse, hub);
    } else {
      return null;
    }
  }

  public HKReachPricingEngine saveHKReachPricingEngine(HKReachPricingEngine hkReachPricingEngine) {
    return (HKReachPricingEngine) courierPricingEngineDao.save(hkReachPricingEngine);
  }

  public CourierDao getCourierDao() {
    return courierDao;
  }

  public void setCourierDao(CourierDao courierDao) {
    this.courierDao = courierDao;
  }

  public UserService getUserService() {
    return userService;
  }
}
