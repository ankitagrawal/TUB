package com.hk.admin.impl.service.courier;

import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.admin.pact.service.courier.CourierPickupService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.admin.factory.courier.thirdParty.ThirdPartyCourierServiceFactory;
import com.hk.pact.dao.BaseDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Feb 7, 2013
 * Time: 11:57:59 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class CourierPickupServiceImpl implements CourierPickupService {

	@Autowired
	BaseDao baseDao;

	public CourierPickupDetail requestCourierPickup (Courier courier, Date pickupDate, String confirmationNo, String trackingNo){
		CourierPickupDetail courierPickupDetail = new CourierPickupDetail();
		courierPickupDetail.setCourier(courier);
		courierPickupDetail.setPickupDate(pickupDate);
		courierPickupDetail.setPickupStatus(EnumPickupStatus.OPEN.asPickupStatus());
		courierPickupDetail.setPickupConfirmationNo(confirmationNo);
		courierPickupDetail.setTrackingNo(trackingNo);
		return courierPickupDetail;
	}

	public CourierPickupDetail save(CourierPickupDetail courierPickupDetail){
		return (CourierPickupDetail) baseDao.save(courierPickupDetail);
	}

	public List<String> getPickupDetailsForThirdParty(Long courierId, ShippingOrder shippingOrder, Date pickupDate){
		ThirdPartyPickupService thirdPartyPickupService = ThirdPartyCourierServiceFactory.getThirdPartyPickupService(courierId);
		return thirdPartyPickupService.createPickupRequest(shippingOrder, pickupDate);
	}


}
