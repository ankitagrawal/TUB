package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 7, 2013
 * Time: 12:11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierPickupService {

	public CourierPickupDetail requestCourierPickup (Courier courier, Date pickupDate, String confirmationNo, String trackingNo);

	public List<String> getPickupDetailsForThirdParty(Long courierId, ShippingOrder shippingOrder, Date pickupDate);

	public CourierPickupDetail save(CourierPickupDetail courierPickupDetail);
}
