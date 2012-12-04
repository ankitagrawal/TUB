package com.hk.admin.impl.service.courier;

import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class DispatchLotServiceImpl implements DispatchLotService {

	@Autowired
	private BaseDao baseDao;

	public DispatchLot saveDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
		                                   String destination, Long noOfShipmentsSent, Long noOfShipmentsReceived, Long noOfMotherBags,
		                                   Double totalWeight, Date deliveryDate) {
		if(dispatchLot != null) {
			dispatchLot.setDocketNumber(docketNumber);
			dispatchLot.setCourier(courier);
			dispatchLot.setZone(zone);
			dispatchLot.setSource(source);
			dispatchLot.setDestination(destination);
			dispatchLot.setNoOfShipmentsSent(noOfShipmentsSent);
			dispatchLot.setNoOfShipmentsReceived(noOfShipmentsReceived);
			dispatchLot.setNoOfMotherBags(noOfMotherBags);
			dispatchLot.setTotalWeight(totalWeight);
			dispatchLot.setDeliveryDate(deliveryDate);
			baseDao.save(dispatchLot);
		}
		return dispatchLot;
	}

	public DispatchLot save(DispatchLot dispatchLot) {
		return (DispatchLot)getBaseDao().save(dispatchLot);
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
