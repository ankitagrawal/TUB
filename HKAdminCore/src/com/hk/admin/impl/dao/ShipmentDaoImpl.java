package com.hk.admin.impl.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Shipment;
import com.hk.impl.dao.BaseDaoImpl;

/**
 * Created by IntelliJ IDEA. User:User Date: Jul 18, 2012 Time: 5:35:39 PM To change this template use File | Settings |
 * File Templates.
 */
@Repository
public class ShipmentDaoImpl extends BaseDaoImpl implements ShipmentDao {
    
    @SuppressWarnings("unchecked")
    public Shipment findByAwb(Awb awb) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Shipment.class);
        criteria.add(Restrictions.eq("awb", awb));
        List<Shipment> shipList = findByCriteria(criteria);
        if (shipList != null && shipList.size() > 0) {
            return shipList.get(0);
        }
        return null;
    }

}
