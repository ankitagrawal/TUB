package com.hk.admin.pact.dao.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.*;
import com.hk.pact.dao.BaseDao;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/5/12
 * Time: 11:39 AM
 * To change this template use File | Settings | File Templates.
 */
public interface DispatchLotDao extends BaseDao {

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, Zone zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, DispatchLotStatus dispatchLotStatus, int pageNo, int perPage);

	public List<Shipment> getShipmentsForDispatchLot(DispatchLot dispatchLot);

	public List<DispatchLot> getDispatchLotsForShipment(Shipment shipment);

	public DispatchLotHasShipment getDispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment);

	public List<Shipment> getShipmentListExistingInOtherActiveDispatchLot(DispatchLot dispatchLot, List<Shipment> shipmentList);

	public List<DispatchLotHasShipment> getDispatchLotHasShipmentListByDispatchLot(DispatchLot dispatchLot);

}
