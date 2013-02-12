package com.hk.impl.dao.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;

import com.hk.domain.courier.PickupStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.ReversePickupDao;
import com.akube.framework.dao.Page;

import java.util.Date;

import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 4, 2012
 * Time: 5:20:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class ReversePickupDaoImpl extends BaseDaoImpl implements ReversePickupDao {

	@Override
	public void savePickupRequest(ShippingOrder shippingOrder, Courier courier, String confirmationNo,
							   Date pickupDate, PickupStatus pickupStatus, ReconciliationStatus reconciliationStatus, User user){
		ReverseOrder reverseOrder = new ReverseOrder();
		reverseOrder.setShippingOrder(shippingOrder);
		reverseOrder.setCourier(courier);
		reverseOrder.setPickupConfirmationNo(confirmationNo);
		reverseOrder.setPickupDate(pickupDate);
		reverseOrder.setPickupStatus(pickupStatus);
		reverseOrder.setReconciliationStatus(reconciliationStatus);
		reverseOrder.setUser(user);
		super.save(reverseOrder);
	}

	@Override
	public void save(ReverseOrder reverseOrder){
		super.save(reverseOrder);
	}

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, int page, int perPage){
        return list(getPickupSearchCriteria(shippingOrderId, pickupStatusId, reconciliationStatusId), page, perPage);
    }

//	 public List<ReverseOrder> getPickupRequestsByStatuses(Boolean pickupStatus, String reconciliationStatus) {
//        return findByCriteria(getPickupSearchCriteria(pickupStatus, reconciliationStatusId));
//    }

	private DetachedCriteria getPickupSearchCriteria(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId) {
        DetachedCriteria pickupCriteria = DetachedCriteria.forClass(ReverseOrder.class);

		if(shippingOrderId != null){
			DetachedCriteria shippingOrderCriteria = pickupCriteria.createCriteria("shippingOrder");
			shippingOrderCriteria.add(Restrictions.eq("gatewayOrderId", shippingOrderId));
		}

        if (pickupStatusId != null){
			DetachedCriteria pickupStatusCriteria = pickupCriteria.createCriteria("pickupStatus");
            pickupStatusCriteria.add(Restrictions.eq("id", pickupStatusId));
		}
		
	    if (reconciliationStatusId != null) {
			DetachedCriteria reconciliationCriteria = pickupCriteria.createCriteria("reconciliationStatus");
			reconciliationCriteria.add(Restrictions.eq("id", reconciliationStatusId));
		}
        return pickupCriteria;
    }
}
