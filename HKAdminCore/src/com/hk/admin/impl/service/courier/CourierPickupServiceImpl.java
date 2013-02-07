package com.hk.admin.impl.service.courier;

import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.courier.Courier;
import com.hk.constants.courier.EnumPickupStatus;
import com.hk.admin.pact.service.courier.CourierPickupService;
import com.hk.pact.dao.BaseDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

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

	public void save(CourierPickupDetail courierPickupDetail){
		baseDao.save(courierPickupDetail);
	}


}
