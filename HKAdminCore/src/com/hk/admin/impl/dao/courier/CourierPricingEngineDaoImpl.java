package com.hk.admin.impl.dao.courier;

import com.hk.domain.core.Pincode;
import com.hk.domain.hkDelivery.HKReachPricingEngine;
import com.hk.domain.hkDelivery.Hub;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 5/25/12
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
@Repository
public class CourierPricingEngineDaoImpl extends BaseDaoImpl implements CourierPricingEngineDao {

  public CourierPricingEngine getCourierPricingInfo(Courier courier, RegionType regionType, Warehouse warehouse){
    Criteria criteria = getSession().createCriteria(CourierPricingEngine.class);
    criteria.add(Restrictions.eq("courier", courier));
    criteria.add(Restrictions.eq("regionType", regionType));
//    criteria.add(Restrictions.eq("warehouse", warehouse));
    return (CourierPricingEngine) criteria.uniqueResult();
  }

  public List<HKReachPricingEngine> getHkReachPricingEngine(Warehouse warehouse, Hub hub, Boolean acceptNull) {
    DetachedCriteria criteria = DetachedCriteria.forClass(HKReachPricingEngine.class);

    if (!acceptNull) {
      if(warehouse == null || hub == null) {
      return null;
      }
    }
    if (warehouse != null) {
      criteria.add(Restrictions.eq("warehouse", warehouse));
    }
    if (hub != null) {
      criteria.add(Restrictions.eq("hub", hub));
    }
    // TODO: add restrictions for active pricing engine

    List<HKReachPricingEngine> result = this.findByCriteria(criteria);

    if (result!=null && !result.isEmpty()) {
      return result;
    } else {
      return null;
    }
  }

  public HKReachPricingEngine getHKHkReachPricingEngineById (Long id) {
    DetachedCriteria criteria = DetachedCriteria.forClass(HKReachPricingEngine.class);
    criteria.add(Restrictions.idEq(id));

    List<HKReachPricingEngine> engines = this.findByCriteria(criteria);

    if (engines != null && !engines.isEmpty()) {
      return engines.get(0);
    } else {
      return null;
    }
  }
}
