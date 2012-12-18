package com.hk.admin.impl.service.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.XslConstants;
import com.hk.constants.courier.DispatchLotConstants;
import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.constants.report.ReportConstants;
import com.hk.domain.courier.*;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
	@Autowired
	HubService hubService;
	@Autowired
	WarehouseService warehouseService;

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, Zone zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, DispatchLotStatus dispatchLotStatus, int pageNo, int perPage) {

		return getDispatchLotDao().searchDispatchLot(dispatchLot, docketNumber, courier, zone, source, destination,
				deliveryStartDate, deliveryEndDate, dispatchLotStatus, pageNo, perPage);
	}

	public void parseExcelAndSaveShipmentDetails(DispatchLot dispatchLot, String excelFilePath, String sheetName) throws ExcelBlankFieldException {
		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
		Iterator<HKRow> rowIterator = parser.parse();
		int rowCount = 1;
		List<String> soGatewayOrderIdInExcel = new ArrayList<String>();

		try {
			//leaving the first row, as it would be empty from the Shipment Awaiting Queue excel
			if(rowIterator.hasNext()) {
				rowIterator.next();
			}

			while (rowIterator.hasNext()) {
				HKRow row = rowIterator.next();
				String soGatewayOrderId = row.getColumnValue(ReportConstants.SHIPPERS_REFERENCE_NUMBER);
				if (StringUtils.isBlank(soGatewayOrderId)) {
					throw new ExcelBlankFieldException("SO Gateway Order Id Cannot be blank", rowCount);
				}
				soGatewayOrderIdInExcel.add(soGatewayOrderId);
				rowCount++;
			}

			//Check if any gatewayOrderId does not exist in the system
			List<ShippingOrder> soListInDB = getAdminShippingOrderDao().getShippingOrderByGatewayOrderList(soGatewayOrderIdInExcel);
			String invalidGatewayOrderIds = validateShippingOrdersForDispatchLot(soGatewayOrderIdInExcel, soListInDB);

			if(invalidGatewayOrderIds != null){
				throw new ExcelBlankFieldException("Following gatewayOrderIds are Invalid : " + invalidGatewayOrderIds);
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


			//check if any shipment exists in other active dispatch lot
			String gatewayOrdersInOtherDispatchLot = checkIfShipmentExistsInOtherDispatchLot(dispatchLot, shipmentList);
			if (gatewayOrdersInOtherDispatchLot != null) {
				throw new ExcelBlankFieldException("Following gatewayOrderIds already exist in other Active Dispatch Lots : " + gatewayOrdersInOtherDispatchLot);
			}

			//Now save the shipments in DispatchLotHasShipment Table.(Bulk update)
			List<DispatchLotHasShipment> dispatchLotHasShipmentList = new ArrayList<DispatchLotHasShipment>(0);

			for(Shipment shipment : shipmentList) {
				/*DispatchLotHasShipment dispatchLotHasShipment = getDispatchLotHasShipment(dispatchLot, shipment);

				if(dispatchLotHasShipment == null) {
					dispatchLotHasShipment = new DispatchLotHasShipment();
				}*/
				DispatchLotHasShipment dispatchLotHasShipment = new DispatchLotHasShipment();
				dispatchLotHasShipment.setDispatchLot(dispatchLot);
				dispatchLotHasShipment.setShipment(shipment);
				dispatchLotHasShipment.setShipmentStatus(DispatchLotConstants.SHIPMENT_DISPATCHED);
				dispatchLotHasShipmentList.add(dispatchLotHasShipment);
			}
			getBaseDao().saveOrUpdate(dispatchLotHasShipmentList);

			//update dispatch Lot
			dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.InTransit.getDispatchLotStatus());
			dispatchLot.setDispatchDate(new Date());
			dispatchLot.setNoOfShipmentsSent((long)dispatchLotHasShipmentList.size());
			getDispatchLotDao().save(dispatchLot);

		} catch (ExcelBlankFieldException e) {
			logger.error("Exception @ Row: " + (rowCount + 1) + e.getMessage());
			throw new ExcelBlankFieldException(e.getMessage());
		}

	}

	private String checkIfShipmentExistsInOtherDispatchLot(DispatchLot dispatchLot, List<Shipment> shipmentList) {
		String gatewayOrderInOtherDispatchLot = null;
		if(shipmentList != null && shipmentList.size() > 0) {
			List<Shipment> existingShipmentInOtherDispatchLots = getDispatchLotDao().getShipmentListExistingInOtherActiveDispatchLot(dispatchLot, shipmentList);

			for(Shipment existingShipment : existingShipmentInOtherDispatchLots) {
				if(gatewayOrderInOtherDispatchLot == null) {
					gatewayOrderInOtherDispatchLot = "";
				}
				gatewayOrderInOtherDispatchLot += existingShipment.getShippingOrder().getGatewayOrderId() + " ";
			}
		}
		return gatewayOrderInOtherDispatchLot;
	}

	public boolean dispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment) {
		List<Shipment> shipmentList = getShipmentsForDispatchLot(dispatchLot);
		if(shipmentList.contains(shipment)){
			return true;
		}
		return false;
	}

	public List<Shipment> getShipmentsForDispatchLot(DispatchLot dispatchLot) {
		return getDispatchLotDao().getShipmentsForDispatchLot(dispatchLot);
	}

	public String markShipmentsAsReceived(DispatchLot dispatchLot, List<String> gatewayOrderIdList) {
		Shipment shipment = null;
		List<ShippingOrder> soListInDB = getAdminShippingOrderDao().getShippingOrderByGatewayOrderList(gatewayOrderIdList);
		String invalidGatewayOrderIds = validateShippingOrdersForDispatchLot(gatewayOrderIdList, soListInDB);
		List<Shipment> validShipmentList = new ArrayList<Shipment>();

		for (ShippingOrder shippingOrder : soListInDB) {
			shipment = shippingOrder.getShipment();
			if (shipment == null) {
				invalidGatewayOrderIds += "  " + shippingOrder.getGatewayOrderId();
			} else if (!dispatchLotHasShipment(dispatchLot, shipment)) {
				invalidGatewayOrderIds += "  " + shippingOrder.getGatewayOrderId();
			} else {
				validShipmentList.add(shipment);
			}
		}

		DispatchLotHasShipment dispatchLotHasShipment=null;
		for(Shipment shipmentTemp : validShipmentList){
			dispatchLotHasShipment = getDispatchLotHasShipment(dispatchLot, shipmentTemp);
			dispatchLotHasShipment.setShipmentStatus(DispatchLotConstants.SHIPMENT_RECEIVED);
			getBaseDao().save(dispatchLotHasShipment);
		}
		long noOfShipmentsReceived = (long)getShipmentsForDispatchLot(dispatchLot).size();

		if(validShipmentList.size() == getShipmentsForDispatchLot(dispatchLot).size()){
			dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.Received.getDispatchLotStatus());
		}
		dispatchLot.setNoOfShipmentsReceived(noOfShipmentsReceived);
		if(dispatchLot.getReceivingDate() == null) {
			dispatchLot.setReceivingDate(new Date());
		}
		getBaseDao().save(dispatchLot);
		
	   return invalidGatewayOrderIds;
	}

	private String validateShippingOrdersForDispatchLot(List<String> gatewayOrderIdList, List<ShippingOrder> soListInDB) {

		String invalidOrders=null;
		if (soListInDB.size() < gatewayOrderIdList.size()) {
			List<String> soGatewayOrderIdInDBList = new ArrayList<String>(0);
			for (ShippingOrder shippingOrder : soListInDB) {
				soGatewayOrderIdInDBList.add(shippingOrder.getGatewayOrderId());
			}
			gatewayOrderIdList.removeAll(soGatewayOrderIdInDBList);
			for (String soGatewayOrderId : gatewayOrderIdList) {
				if(invalidOrders == null) {
					invalidOrders = "";
				}
				invalidOrders += soGatewayOrderId + " ";
			}
		}
		return invalidOrders;
	}

	public List<String> getSourceAndDestinationListForDispatchLot() {
		List <String> validSourceAndDestinations = new ArrayList<String>();
		List<Hub> hubList = hubService.getAllHubs();
		List<Warehouse> warehouseList = warehouseService.getServiceableWarehouses();
		for (Warehouse warehouse : warehouseList){
			if(warehouse != null && warehouse.getName() != null){
				validSourceAndDestinations.add(warehouse.getName());
			}
		}

		for(Hub hub : hubList){
			if(hub != null && hub.getName() != null){
				validSourceAndDestinations.add(hub.getName());
			}
		}
		return validSourceAndDestinations; 
	}

	public DispatchLot cancelDispatchLot(DispatchLot dispatchLot) {
		if(dispatchLot != null) {
			dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.Cancelled.getDispatchLotStatus());
			dispatchLot = (DispatchLot)getDispatchLotDao().save(dispatchLot);
		}
		return dispatchLot;
	}

	public List<DispatchLot> getDispatchLotsForShipment(Shipment shipment) {
		return getDispatchLotDao().getDispatchLotsForShipment(shipment);
	}

	public DispatchLotHasShipment getDispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment){
		return getDispatchLotDao().getDispatchLotHasShipment(dispatchLot, shipment);
	}

	public List<DispatchLotHasShipment> getDispatchLotHasShipmentListByDispatchLot(DispatchLot dispatchLot) {
		return getDispatchLotDao().getDispatchLotHasShipmentListByDispatchLot(dispatchLot);
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
