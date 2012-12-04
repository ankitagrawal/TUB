package com.hk.admin.pact.service.courier;

import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/4/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DispatchLotService {

	public DispatchLot saveDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
	                                   String destination, Long noOfShipmentsSent, Long noOfShipmentsReceived, Long noOfMotherBags,
	                                   Double totalWeight, Date deliveryDate);

	public DispatchLot save(DispatchLot dispatchLot);

}
