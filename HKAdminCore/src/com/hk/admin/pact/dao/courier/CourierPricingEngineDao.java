package com.hk.admin.pact.dao.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 1:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierPricingEngineDao extends BaseDao {

  public CourierPricingEngine getCourierPricingInfo(Courier courier, RegionType regionType, Warehouse warehouse);

  public List<HKReachPricingEngine> getHkReachPricingEngine(Warehouse warehouse, Hub hub);

  public HKReachPricingEngine getHKHkReachPricingEngineById (Long id);

}
