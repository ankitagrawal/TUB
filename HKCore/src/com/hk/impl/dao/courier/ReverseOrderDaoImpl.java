package com.hk.impl.dao.courier;


import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.ReverseOrderDao;
import com.akube.framework.dao.Page;
import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by IntelliJ IDEA.  * User: Neha * Date: Dec 4, 2012 * Time: 5:20:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReverseOrderDaoImpl extends BaseDaoImpl implements ReverseOrderDao {
    
	@Override
	public ReverseOrder save(ReverseOrder reverseOrder){
		return (ReverseOrder) super.save(reverseOrder);
	}

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, int page, int perPage){
        return list(getPickupSearchCriteria(shippingOrderId, pickupStatusId, reconciliationStatusId), page, perPage);
    }

//	 public List<ReverseOrder> getPickupRequestsByStatuses(Boolean pickupStatus, String reconciliationStatus) {
//        return findByCriteria(getPickupSearchCriteria(pickupStatus, reconciliationStatusId));
//    }

	private DetachedCriteria getPickupSearchCriteria(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId) {
        DetachedCriteria orderCriteria = DetachedCriteria.forClass(ReverseOrder.class);

		if(shippingOrderId != null){
			DetachedCriteria shippingOrderCriteria = orderCriteria.createCriteria("shippingOrder");
			shippingOrderCriteria.add(Restrictions.eq("gatewayOrderId", shippingOrderId));
		}

        if (pickupStatusId != null){
			DetachedCriteria pickupStatusCriteria = orderCriteria.createCriteria("courierPickupDetail").createCriteria("pickupStatus");
            pickupStatusCriteria.add(Restrictions.eq("id", pickupStatusId));
		}
		
	    if (reconciliationStatusId != null) {
			DetachedCriteria reconciliationCriteria = orderCriteria.createCriteria("reconciliationStatus");
			reconciliationCriteria.add(Restrictions.eq("id", reconciliationStatusId));
		}
        return orderCriteria;
    }

	public ReverseOrder getReverseOrderById(Long id){
		return (ReverseOrder) super.findUniqueByNamedQueryAndNamedParam("getReverseOrderById", new String[]{"reverseOrderId"}, new Object[]{id});
	}
}
