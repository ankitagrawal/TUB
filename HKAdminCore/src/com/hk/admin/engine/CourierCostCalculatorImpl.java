package com.hk.admin.engine;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.admin.pact.service.courier.*;
import com.hk.comparator.MapValueComparator;
import com.hk.constants.courier.EnumCourier;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierGroup;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.PincodeRegionZone;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.util.NumberUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 5/25/12
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CourierCostCalculatorImpl implements CourierCostCalculator {

  private static Logger logger = LoggerFactory.getLogger(CourierCostCalculatorImpl.class);

  @Autowired
  PincodeDao pincodeDao;

  @Autowired
  PincodeRegionZoneService pincodeRegionZoneService;

  @Autowired
  CourierPricingEngineDao courierPricingEngineDao;

  @Autowired
  ShipmentPricingEngine shipmentPricingEngine;

  @Autowired
  CourierGroupService courierGroupService;

  @Autowired
  private PincodeCourierService pincodeCourierService;

  @Autowired
  ShippingOrderDao shippingOrderDao;

  @Autowired
  CourierService courierService;

  public Courier getCheapestCourier(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground) {
    Map.Entry<Courier, Long> courierCostingMapEntry = getCheapestCourierEntry(pincode, cod, srcWarehouse, amount, weight, ground);
    return courierCostingMapEntry.getKey();
  }

  public Long getCheapestCourierCost(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground) {
    Map.Entry<Courier, Long> courierCostingMapEntry = getCheapestCourierEntry(pincode, cod, srcWarehouse, amount, weight, ground);
    return courierCostingMapEntry.getValue();
  }

  public Map.Entry<Courier, Long> getCheapestCourierEntry(String pincode, boolean cod, Warehouse srcWarehouse, Double amount, Double weight, boolean ground) {
    TreeMap<Courier, Long> courierCostingMap = getCourierCostingMap(pincode, cod, srcWarehouse, amount, weight, ground,null);
    return courierCostingMap.lastEntry();
  }

  @SuppressWarnings("unchecked")
  public TreeMap<Courier, Long> getCourierCostingMap(String pincode, boolean cod, Warehouse srcWarehouse, Double amount,
                                                     Double weight, boolean ground, Date shipmentDate) {
    Pincode pincodeObj = pincodeDao.getByPincode(pincode);
    List<Courier> applicableCourierList = pincodeCourierService.getApplicableCouriers(pincodeObj, cod, ground, true);
    Double totalCost = 0D;

      if (pincodeObj == null || applicableCourierList == null || applicableCourierList.isEmpty()) {
          logger.error("Could not fetch applicable couriers while making courier costing map for pincode " + pincode
              + "cod " + cod + " ground " + ground);
          return new TreeMap<Courier, Long>();
      }
      List<PincodeRegionZone> sortedApplicableZoneList =
                    pincodeRegionZoneService.getApplicableRegionList(applicableCourierList, pincodeObj, srcWarehouse);
    Map<Courier, Long> courierCostingMap = new HashMap<Courier, Long>();
    for (PincodeRegionZone pincodeRegionZone : sortedApplicableZoneList) {
      Set<Courier> couriers = courierGroupService.getCommonCouriers(pincodeRegionZone.getCourierGroup(),
          applicableCourierList);
      for (Courier courier : couriers) {
        if (EnumCourier.HK_Delivery.getId().equals(courier.getId())) {
          if (pincodeObj.getNearestHub() != null) {
            HKReachPricingEngine hkReachPricingEngine = courierService.getHkReachPricingEngine(srcWarehouse,
                pincodeObj.getNearestHub(), shipmentDate);
            if(hkReachPricingEngine != null){
              totalCost = shipmentPricingEngine.calculateHKReachCost(hkReachPricingEngine, weight, pincodeObj);
            }
          } else {
            totalCost = -1d;
          }
        } else {
          CourierPricingEngine courierPricingInfo = courierPricingEngineDao.getCourierPricingInfo(courier,
              pincodeRegionZone.getRegionType(), srcWarehouse, shipmentDate);
          if (courierPricingInfo == null) {
            continue;
          }
          totalCost = shipmentPricingEngine.calculateShipmentCost(courierPricingInfo, weight);
        }
        logger.debug("courier " + courier.getName() + "totalCost " + totalCost);
        courierCostingMap.put(courier, Math.round(totalCost));
      }
    }

    MapValueComparator mapValueComparator = new MapValueComparator(courierCostingMap);
      TreeMap<Courier, Long> sortedCourierCostingTreeMap = new TreeMap(mapValueComparator);
      sortedCourierCostingTreeMap.putAll(courierCostingMap);

      return sortedCourierCostingTreeMap;
  }

  public CourierPricingEngine getCourierPricingInfo(Courier courier, Pincode pincodeObj, Warehouse srcWarehouse,
                                                    Date shipmentDate) {
    CourierGroup courierGroup = courierGroupService.getCourierGroup(courier);
    PincodeRegionZone pincodeRegionZone = pincodeRegionZoneService.getPincodeRegionZone(courierGroup, pincodeObj,
        srcWarehouse);
    if (pincodeRegionZone == null) {
      if (courierGroup == null) {
        logger.error("courier group not found for courier " + courier.getName());
        return null;
      }
      logger.info("prz null for " + pincodeObj.getPincode() + courierGroup.getName() + srcWarehouse.getCity());
      return null;
    }
    return courierPricingEngineDao.getCourierPricingInfo(courier, pincodeRegionZone.getRegionType(),
                                                                                          srcWarehouse, shipmentDate);
  }
}

