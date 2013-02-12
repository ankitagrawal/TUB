package com.hk.pact.dao.courier;

import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;

import com.hk.domain.courier.PickupStatus;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.user.User;
import com.akube.framework.dao.Page;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Dec 4, 2012
 * Time: 5:23:20 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReversePickupDao {
	public void savePickupRequest(ShippingOrder shippingOrder, Courier courier, String confirmationNo,
							   Date pickupDate, PickupStatus pickupStatus, ReconciliationStatus reconciliationStatus, User user);

	public void save(ReverseOrder reverseOrder);

	public Page getPickupRequestsByStatuses(String shippingOrderId, Long pickupStatusId, Long reconciliationStatusId, int page, int perPage);

	//public List<ReverseOrder> getPickupRequestsByStatuses(Boolean pickupStatus, String reconciliationStatus);


}
