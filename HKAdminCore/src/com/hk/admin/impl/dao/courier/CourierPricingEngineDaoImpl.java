package com.hk.admin.impl.dao.courier;

import com.hk.admin.pact.dao.courier.CourierPricingEngineDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPricingEngine;
import com.hk.domain.courier.RegionType;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.BaseDaoImpl;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

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

}
