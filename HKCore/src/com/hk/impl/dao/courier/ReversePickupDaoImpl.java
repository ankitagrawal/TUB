package com.hk.impl.dao.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.ReversePickup;
import com.hk.domain.courier.PickupStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.ReversePickupDao;
import com.hk.constants.inventory.EnumReconciliationStatus;
import com.akube.framework.dao.Page;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.Criteria;
import org.apache.commons.lang.StringUtils;

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
		ReversePickup reversePickup = new ReversePickup();
		reversePickup.setShippingOrder(shippingOrder);
		reversePickup.setCourier(courier);
		reversePickup.setPickupConfirmationNo(confirmationNo);
		reversePickup.setPickupDate(pickupDate);
		reversePickup.setPickupStatus(pickupStatus);
		reversePickup.setReconciliationStatus(reconciliationStatus);
		reversePickup.setUser(user);
		super.save(reversePickup);
	}

	@Override
	public void save(ReversePickup reversePickup){
		super.save(reversePickup);
	}

	public Page getPickupRequestsByStatuses(Long shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, int page, int perPage){
        return list(getPickupSearchCriteria(shippingOrderId, pickupStatusId, reconciliationStatusId), page, perPage);
    }

//	 public List<ReversePickup> getPickupRequestsByStatuses(Boolean pickupStatus, String reconciliationStatus) {
//        return findByCriteria(getPickupSearchCriteria(pickupStatus, reconciliationStatusId));
//    }

	private DetachedCriteria getPickupSearchCriteria(Long shippingOrderId, Long pickupStatusId, Long reconciliationStatusId) {
        DetachedCriteria pickupCriteria = DetachedCriteria.forClass(ReversePickup.class);

		if(shippingOrderId != null){
			DetachedCriteria shippingOrderCriteria = DetachedCriteria.forClass(ShippingOrder.class);
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
