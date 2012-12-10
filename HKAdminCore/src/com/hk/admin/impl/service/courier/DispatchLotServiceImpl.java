package com.hk.admin.impl.service.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.constants.XslConstants;
import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.domain.courier.DispatchLotHasShipment;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class DispatchLotServiceImpl implements DispatchLotService {

	private static Logger logger = LoggerFactory.getLogger(DispatchLotServiceImpl.class);

	@Autowired
	private DispatchLotDao dispatchLotDao;
	@Autowired
	private AdminShippingOrderDao adminShippingOrderDao;
	@Autowired
	private BaseDao baseDao;

	/*public DispatchLot saveDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
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
	*/
	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, int pageNo, int perPage) {

		return getDispatchLotDao().searchDispatchLot(dispatchLot, docketNumber, courier, zone, source, destination,
				deliveryStartDate, deliveryEndDate, pageNo, perPage);
	}

	public void parseExcelAndSaveShipmentDetails(DispatchLot dispatchLot, String excelFilePath, String sheetName) throws ExcelBlankFieldException {
		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
		Iterator<HKRow> rowIterator = parser.parse();
		int rowCount = 1;
		List<String> soGatewayOrderIdInExcel = new ArrayList<String>();
		try {
			while (rowIterator.hasNext()) {
				HKRow row = rowIterator.next();
				String soGatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
				if (StringUtils.isBlank(soGatewayOrderId)) {
					throw new ExcelBlankFieldException("SO Gateway Order Id Cannot be blank", rowCount);
				}

				soGatewayOrderIdInExcel.add(soGatewayOrderId);
				rowCount++;
			}

			//Check if any gatewayOrderId does not exist in the system
			List<ShippingOrder> soListInDB = getAdminShippingOrderDao().getShippingOrderByGatewayOrderList(soGatewayOrderIdInExcel);
			if (soListInDB.size() < soGatewayOrderIdInExcel.size()) {
				List<String> soGatewayOrderIdInDBList = new ArrayList<String>(0);
				for (ShippingOrder shippingOrder : soListInDB) {
					soGatewayOrderIdInDBList.add(shippingOrder.getGatewayOrderId());
				}
				soGatewayOrderIdInExcel.removeAll(soGatewayOrderIdInDBList);
				String invalidOrders = "";
				for (String soGatewayOrderId : soGatewayOrderIdInExcel) {
					invalidOrders += soGatewayOrderId + " ";
				}
				throw new ExcelBlankFieldException("Following gatewayOrderIds are Invalid : " + invalidOrders);
			}

			//Check if zone is not the Selected Zone of the Dispatch Lot
			List<Shipment> shipmentList = new ArrayList<Shipment>(0);
			String invalidOrdersByZone = "";
			boolean differentZone = false;
			boolean shipmentExists = true;
			String invalidOrdersByShipment = "";

			for (ShippingOrder shippingOrder : soListInDB) {
				if(shippingOrder.getShipment() == null) {
					shipmentExists = false;
					invalidOrdersByShipment += shippingOrder.getGatewayOrderId();
				} else if(!dispatchLot.getZone().equals(shippingOrder.getShipment().getZone())) {
					differentZone = true;
					invalidOrdersByZone += shippingOrder.getGatewayOrderId() + " ";
				}
				shipmentList.add(shippingOrder.getShipment());
			}
			if(!shipmentExists) {
				throw new ExcelBlankFieldException("Shipments for the following gatewayOrderIds does not exist: " + invalidOrdersByShipment);
			}
			if(differentZone) {
				throw new ExcelBlankFieldException("Following gatewayOrderIds belong to a different zone : " + invalidOrdersByZone);
			}

			//Now save the shipments in DispatchLotHasShipment Table.(Bulk update)
			List<DispatchLotHasShipment> dispatchLotHasShipmentList = new ArrayList<DispatchLotHasShipment>(0);
			for(Shipment shipment : shipmentList) {
				DispatchLotHasShipment dispatchLotHasShipment = new DispatchLotHasShipment();
				dispatchLotHasShipment.setDispatchLot(dispatchLot);
				dispatchLotHasShipment.setShipment(shipment);
				dispatchLotHasShipmentList.add(dispatchLotHasShipment);
			}
			getBaseDao().saveOrUpdate(dispatchLotHasShipmentList);

			//update dispatch Lot
			dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.InTransit.getDispatchLotStatus());
			dispatchLot.setDispatchDate(new Date());
			getDispatchLotDao().save(dispatchLot);

		} catch (ExcelBlankFieldException e) {
			logger.error("Exception @ Row: " + (rowCount + 1) + e.getMessage());
			throw new ExcelBlankFieldException(e.getMessage());
		}

	}

	public DispatchLot save(DispatchLot dispatchLot) {
		return (DispatchLot) getDispatchLotDao().save(dispatchLot);
	}

	public DispatchLotDao getDispatchLotDao() {
		return dispatchLotDao;
	}

	public void setDispatchLotDao(DispatchLotDao dispatchLotDao) {
		this.dispatchLotDao = dispatchLotDao;
	}

	public AdminShippingOrderDao getAdminShippingOrderDao() {
		return adminShippingOrderDao;
	}

	public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
		this.adminShippingOrderDao = adminShippingOrderDao;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}
}
