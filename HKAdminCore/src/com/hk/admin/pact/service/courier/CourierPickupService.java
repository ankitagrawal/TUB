package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 7, 2013
 * Time: 12:11:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface CourierPickupService {

	public CourierPickupDetail requestCourierPickup (Courier courier, Date pickupDate, String confirmationNo, String trackingNo);

	public CourierPickupDetail save(CourierPickupDetail courierPickupDetail);
}
