package com.hk.admin.impl.service.courier;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.hk.constants.XslConstants;
import com.hk.util.io.HkXlsWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.courier.DispatchLotConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import com.hk.constants.report.ReportConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.domain.courier.DispatchLotHasShipment;
import com.hk.domain.courier.DispatchLotStatus;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Zone;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.hkDelivery.ConsignmentLifecycleStatus;
import com.hk.domain.hkDelivery.ConsignmentTracking;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

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
	@Autowired
	UserService userService;
	@Autowired
	ConsignmentService consignmentService;

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
			//skipping the first row, as it would be empty in the excel being used (Shipment Awaiting Queue excel)
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

			if(invalidGatewayOrderIds !=null){
				throw new ExcelBlankFieldException("Following gatewayOrderIds are Invalid : " + invalidGatewayOrderIds);
			}

			//Check if zone is not the Selected Zone of the Dispatch Lot
			List<Shipment> shipmentList = new ArrayList<Shipment>(0);
			String invalidOrdersByZone = "";
			boolean differentZone = false;
			boolean shipmentExists = true;
			String invalidOrdersByShipment = "";

			for (ShippingOrder shippingOrder : soListInDB) {

				if(shippingOrder.getShipment() == null || shippingOrder.getOrderStatus().getId() < EnumShippingOrderStatus.SO_Shipped.getId()) {
					shipmentExists = false;
					invalidOrdersByShipment += shippingOrder.getGatewayOrderId();
				} else if(!dispatchLot.getZone().equals(shippingOrder.getShipment().getZone())) {
					differentZone = true;
					invalidOrdersByZone += shippingOrder.getGatewayOrderId() + " ";
				}
				shipmentList.add(shippingOrder.getShipment());
			}
			if(!shipmentExists) {
				throw new ExcelBlankFieldException("Either Shipments for the following gatewayOrderIds does not exist, or shipping order status is less than Shipped: "
						+ invalidOrdersByShipment);
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
			//dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.InTransit.getDispatchLotStatus());
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

	public File generateDispatchLotExcel(File xlsFile, List<DispatchLotHasShipment> dispatchLotHasShipmentList) {
		HkXlsWriter xlsWriter = new HkXlsWriter();
		if(dispatchLotHasShipmentList != null) {
			int xlsRow = 1;
			xlsWriter.addHeader(XslConstants.GATEWAY_ORDER_ID, XslConstants.GATEWAY_ORDER_ID);
			xlsWriter.addHeader(XslConstants.AWB_NUMBER, XslConstants.AWB_NUMBER);
			xlsWriter.addHeader(XslConstants.SHIPMENT_STATUS, XslConstants.SHIPMENT_STATUS);
			for (DispatchLotHasShipment dispatchLotHasShipment : dispatchLotHasShipmentList) {
				xlsWriter.addCell(xlsRow, dispatchLotHasShipment.getShipment().getShippingOrder().getGatewayOrderId());
				xlsWriter.addCell(xlsRow, dispatchLotHasShipment.getShipment().getAwb().getAwbNumber());
				xlsWriter.addCell(xlsRow, dispatchLotHasShipment.getShipmentStatus());
				xlsRow++;
			}
			xlsWriter.writeData(xlsFile, "Sheet1");

		}
		return xlsFile;
	}

	public List<Shipment> getShipmentsForDispatchLot(DispatchLot dispatchLot) {
		return getDispatchLotDao().getShipmentsForDispatchLot(dispatchLot);
	}

	public String markShipmentsAsReceived(DispatchLot dispatchLot, List<String> gatewayOrderIdList) {
		Shipment shipment = null;
		List<ShippingOrder> soListInDB = getAdminShippingOrderDao().getShippingOrderByGatewayOrderList(gatewayOrderIdList);
		String invalidGatewayOrderIds = validateShippingOrdersForDispatchLot(gatewayOrderIdList, soListInDB);
		Set<Shipment> validShipmentSet = new HashSet<Shipment>(0);
		for (ShippingOrder shippingOrder : soListInDB) {
			shipment = shippingOrder.getShipment();
			if (shipment == null) {
				invalidGatewayOrderIds += "  " + shippingOrder.getGatewayOrderId();
			} else if (!dispatchLotHasShipment(dispatchLot, shipment)) {
				invalidGatewayOrderIds += "  " + shippingOrder.getGatewayOrderId();
			} else if(getDispatchLotHasShipment(dispatchLot, shipment).getShipmentStatus().equalsIgnoreCase(DispatchLotConstants.SHIPMENT_RECEIVED)){
				invalidGatewayOrderIds += "  " + shippingOrder.getGatewayOrderId();
			}
			else if(getDispatchLotHasShipment(dispatchLot, shipment).getShipmentStatus().equalsIgnoreCase(DispatchLotConstants.SHIPMENT_DISPATCHED)) {
				validShipmentSet.add(shipment);
			}
		}

		if(validShipmentSet.size() == 0){
			return invalidGatewayOrderIds;
		}

		DispatchLotHasShipment dispatchLotHasShipment=null;
		//Parameters need to create consignment for hkdelivery
		List<Consignment> consignmentList = new ArrayList<Consignment>();
		List<String> awbNumbers = new ArrayList<String>();          // Needed to update consignment tracking once consinmgnment list is saved, need to optimize code
		Consignment consignment = null;
		Hub healthkartHub=null;
		Hub hub=null;
		User loggedInUser = userService.getLoggedInUser();
		ConsignmentLifecycleStatus consignmentLifecycleStatus = getBaseDao().get(ConsignmentLifecycleStatus.class, EnumConsignmentLifecycleStatus.ReceivedAtHub.getId());

		List<DispatchLotHasShipment> dispatchLotHasShipmentList = new ArrayList<DispatchLotHasShipment>();


		for(Shipment shipmentTemp : validShipmentSet){
			dispatchLotHasShipment = getDispatchLotHasShipment(dispatchLot, shipmentTemp);
			dispatchLotHasShipment.setShipmentStatus(DispatchLotConstants.SHIPMENT_RECEIVED);
			dispatchLotHasShipmentList.add(dispatchLotHasShipment);
			if(shipmentTemp.getAwb().getCourier().getId().equals(EnumCourier.HK_Delivery.getId())){
				hub = hubService.findHubByName(dispatchLot.getDestination());
				healthkartHub = hubService.findHubByName(HKDeliveryConstants.HEALTHKART_HUB);
				//if destination is a hub and shipment is of hkdelivery, create consignment for the same
				if(hub != null){
					ShippingOrder shippingOrder = shipmentTemp.getShippingOrder();
					String paymentMode = consignmentService.getConsignmentPaymentMode(shippingOrder);
					String address = shipmentTemp.getShippingOrder().getBaseOrder().getAddress().getLine1()+","+shipmentTemp.getShippingOrder().getBaseOrder().getAddress().getLine2()+","+
                        shipmentTemp.getShippingOrder().getBaseOrder().getAddress().getCity()+"-"+shipmentTemp.getShippingOrder().getBaseOrder().getAddress().getPincode().getPincode();
					consignment = consignmentService.createConsignment(shipmentTemp.getAwb().getAwbNumber(), shippingOrder.getGatewayOrderId(), shippingOrder.getAmount(), paymentMode,
					address, hub);
					consignmentList.add(consignment);
					awbNumbers.add(consignment.getAwbNumber());
				}
			}
		}

		getBaseDao().saveOrUpdate(dispatchLotHasShipmentList);
		//saving consingnment list and adding tracking entries for the same
		if(consignmentList.size() > 0 && !consignmentList.contains(null)){
			consignmentService.saveConsignments(consignmentList);
			consignmentList = consignmentService.getConsignmentListByAwbNumbers(awbNumbers);
			List<ConsignmentTracking> consignmentTrackingList = consignmentService.createConsignmentTracking(healthkartHub,hub,loggedInUser,consignmentList ,consignmentLifecycleStatus, null);
			consignmentService.saveConsignmentTracking(consignmentTrackingList);
		}
		//long dispatchLotSize = (long)getShipmentsForDispatchLot(dispatchLot).size();
		dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.PartiallyReceived.getDispatchLotStatus());

		long currentReceivedShipmentSize = getDispatchLotHasShipmentListByDispatchLot(dispatchLot, DispatchLotConstants.SHIPMENT_RECEIVED).size();

		dispatchLot.setNoOfShipmentsReceived(currentReceivedShipmentSize);
		if(currentReceivedShipmentSize == getDispatchLotDao().getDispatchLotHasShipmentListByDispatchLot(dispatchLot).size()) {
			markDispatchLotReceived(dispatchLot);
			return invalidGatewayOrderIds;
		}

	   getBaseDao().save(dispatchLot);
	   return invalidGatewayOrderIds;
	}

	public boolean markDispatchLotReceived(DispatchLot dispatchLot){
		List<DispatchLotHasShipment> dispatchLotHasShipmentList;
		dispatchLotHasShipmentList = getDispatchLotHasShipmentListByDispatchLot(dispatchLot, null);
		User loggedInUser = userService.getLoggedInUser();
		List<DispatchLotHasShipment> lostShipments = new ArrayList<DispatchLotHasShipment>();
		for(DispatchLotHasShipment dispatchLotHasShipment : dispatchLotHasShipmentList){
			if(dispatchLotHasShipment.getShipmentStatus().equals(DispatchLotConstants.SHIPMENT_DISPATCHED)){
				dispatchLotHasShipment.setShipmentStatus(DispatchLotConstants.SHIPMENT_LOST);
				lostShipments.add(dispatchLotHasShipment);
			}
		}
		dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.Received.getDispatchLotStatus());
		dispatchLot.setReceivingDate(new Date());
		dispatchLot.setReceivedBy(loggedInUser);
		if(lostShipments.size()>0){
			getBaseDao().saveOrUpdate(lostShipments);
		}
		getBaseDao().save(dispatchLot);
		return true;
	}

	public List<String> getShipmentStatusForDispatchLot() {
		List<String> shipmentStatusList = new ArrayList<String>();
		shipmentStatusList.add(DispatchLotConstants.SHIPMENT_DISPATCHED);
		shipmentStatusList.add(DispatchLotConstants.SHIPMENT_LOST);
		shipmentStatusList.add(DispatchLotConstants.SHIPMENT_RECEIVED);
		return shipmentStatusList;
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
			if(warehouse != null && warehouse.getIdentifier() != null){
				validSourceAndDestinations.add(warehouse.getIdentifier());
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
			/***
			 * TODO: mark shipments in this dispatch lot also cancelled.
			 */
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

	public List<DispatchLotHasShipment> getDispatchLotHasShipmentListByDispatchLot(DispatchLot dispatchLot, String shipmentStatus) {
	    
	    /**
	     * TODO: can be done via a query only
	     */
		List<DispatchLotHasShipment> dispatchLotHasShipmentList = new ArrayList<DispatchLotHasShipment>(0);
		dispatchLotHasShipmentList = getDispatchLotDao().getDispatchLotHasShipmentListByDispatchLot(dispatchLot);
		List<DispatchLotHasShipment> filteredDispatchLotHasShipmentList = new ArrayList<DispatchLotHasShipment>();
		if(shipmentStatus != null && !shipmentStatus.equals("")){
			for(DispatchLotHasShipment dispatchLotHasShipment : dispatchLotHasShipmentList){
				if(dispatchLotHasShipment.getShipmentStatus().equals(shipmentStatus)){
					filteredDispatchLotHasShipmentList.add(dispatchLotHasShipment);
				}
			}
			return filteredDispatchLotHasShipmentList;
		}
		return dispatchLotHasShipmentList;
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
