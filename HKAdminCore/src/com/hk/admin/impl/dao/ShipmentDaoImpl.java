package com.hk.admin.impl.dao;

import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.impl.dao.BaseDaoImpl;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jul 18, 2012
 * Time: 5:35:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ShipmentDaoImpl extends BaseDaoImpl implements ShipmentDao {

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
