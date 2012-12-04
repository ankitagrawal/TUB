package com.hk.impl.dao.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.ReversePickup;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;
import com.hk.impl.dao.BaseDaoImpl;
import com.hk.pact.dao.courier.ReversePickupDao;
import com.akube.framework.dao.Page;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
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
							   Date pickupDate, boolean pickupStatus, ReconciliationStatus reconciliationStatus, User user){
		ReversePickup reversePickup = new ReversePickup();
		reversePickup.setShippingOrder(shippingOrder);
		reversePickup.setCourier(courier);
		reversePickup.setPickupConfirmationNo(confirmationNo);
		reversePickup.setPickupDate(pickupDate);
		reversePickup.setPickupStatus(pickupStatus);
		reversePickup.setReconciliationStatus(reconciliationStatus);
		reversePickup.setUser(user);
		save(reversePickup);
	}

//	public List<ReversePickup> getPickupRequestsList(){
//
//		return (List<ReversePickup>) findByNamedQuery("from ReversePickup rp where rp.pickupStatus = 0 or rp.reconciliationStatus = 10");
//	}

	public Page getPickupRequestsByStatuses(Boolean pickupStatus, Boolean reconciliationStatus, int page, int perPage){
        return list(getPickupSearchCriteria(pickupStatus, reconciliationStatus), page, perPage);
    }

	 public List<ReversePickup> getPickupRequestsByStatuses(Boolean pickupStatus, Boolean reconciliationStatus) {
        return findByCriteria(getPickupSearchCriteria(pickupStatus, reconciliationStatus));
    }

	private DetachedCriteria getPickupSearchCriteria(Boolean pickupStatus, Boolean reconciliationStatus) {
        DetachedCriteria criteria = DetachedCriteria.forClass(ReversePickup.class);

        if (pickupStatus != null)
            criteria.add(Restrictions.eq("pickupStatus", pickupStatus));

	    if (reconciliationStatus != null)
            criteria.add(Restrictions.eq("reconciliationStatus", reconciliationStatus));

        return criteria;
    }
}
